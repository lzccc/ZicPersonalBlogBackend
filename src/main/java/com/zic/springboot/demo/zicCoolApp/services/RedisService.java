package com.zic.springboot.demo.zicCoolApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

//The Lazy is just tell Spring that don't initialize bean if it is not required.
@Service
@Lazy
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private LocalFileService localFileService;

    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void setValueWithTTL(String key, Object value, long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void storeHash(String key, Map<String, Object> hash) {
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.putAll(key, hash);
    }

    public Map<String, Object> getHash(String key) {
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries(key);
    }

    public void storeListOfHashes(String key, List<String> hashKeys) {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        for (String hashKey : hashKeys) {
            if (hasKey(hashKey)) {
                listOperations.leftPush(key, hashKey);
            }
        }
    }

    public void pushToList(String listKey, String newKey) {
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        if (hasKey(newKey)) {
            listOperations.leftPush(listKey, newKey);
        }
    }

    public List<Object> getList(String key) {
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        return listOperations.range(key, 0, -1);
    }

    public List<Object> getAllFromZSet(String key) {
        return new ArrayList<>(redisTemplate.opsForZSet().reverseRange(key, 0, -1));
    }

    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    //Set a max number of retries
    public void deleteBlog(String blogId) {
        redisTemplate.execute(new SessionCallback<>() {
            public Object execute(RedisOperations operations) throws DataAccessException {
                Set<String> keys = redisTemplate.keys("*" + blogId+ "*");

                operations.watch(keys);
                operations.multi();
                //delete blog meta
                operations.delete(blogId);
                //delete blog meta from zset

                //ToDo: change this hardcoded user1
                operations.opsForZSet().remove("blogSet:user1", blogId);
                //delete content, delete it from redis and from disk
                operations.delete("blogContent:" + blogId);
                localFileService.deleteContent("blogContent:" + blogId);

                //Return the list of replies for each executed command.
                List<Object> results = operations.exec();

                //ToDo: May need to change this to check the result of each command.
                if (results == null) {
                    deleteBlog(blogId);
                }
                return null;
            }
        });
    }

    public String createUserKey(String userId, Map<String, Object> request) {
        String prefix = "blog:" + userId;
        return redisTemplate.execute(new SessionCallback<String>() {
            public String execute(RedisOperations operations) throws DataAccessException {
                String keyPattern = prefix + ":*";
                operations.watch(keyPattern); // Watch for changes
                operations.multi(); // Begin transaction
                // Add new key
                UUID uuid = UUID.randomUUID();
                String newKey = prefix + ":" + uuid;
                request.put("blogId", newKey);
                Date currDate = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy");

                // Format the date
                String dateString = formatter.format(currDate);
                request.put("date", dateString);

                //Set blog metadata
                operations.opsForHash().putAll(newKey, request);

                //Add this blogID to the blog zset of this user
                operations.opsForZSet().add("blogSet:" + userId, newKey, currDate.getTime());

                //create the actual content key for this blog
                String fileContent = (String)request.get("fileContent");

                //Remove this key to save some extra space
                request.remove("fileContent");

                //Comment this out to use disk for saving file content
                // operations.opsForValue().set("blogContent:" + newKey, fileContent);
                localFileService.saveContent("blogContent:" + newKey, fileContent);

                // If EXEC returns null, a watched key has been modified and operation should be retried
                List<Object> results = operations.exec();
                if (results == null) {
                    return createUserKey(userId, request);
                }
                return newKey;
            }
        });
    }

    public String createComment(String userId, Map<String, Object> request) {
        String prefix = userId + ":comment";
        return redisTemplate.execute(new SessionCallback<String>() {
            public String execute(RedisOperations operations) throws DataAccessException {
                operations.multi(); // Begin transaction
                // Add new key
                UUID uuid = UUID.randomUUID();
                String newKey = prefix + ":" + uuid;
                request.put("commentId", newKey);
                Date currDate = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy HH:mm:ss");
                // Format the date
                String dateString = formatter.format(currDate);
                request.put("date", dateString);
                //Set comment metadata
                operations.opsForHash().putAll(newKey, request);
                //Add this blogID to the blog zset of this user
                operations.opsForZSet().add("commentSet:" + userId, newKey, currDate.getTime());
                // If EXEC returns null, a watched key has been modified and operation should be retried
                List<Object> results = operations.exec();
                if (results == null) {
                    return createComment(userId, request);
                }
                return newKey;
            }
        });
    }

    public void deleteComment(String commentId) {
        redisTemplate.execute(new SessionCallback<>() {
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                //delete blog meta
                operations.delete(commentId);
                //delete blog meta from zset

                //ToDo: change this hardcoded user1
                operations.opsForZSet().remove("commentSet:user1", commentId);

                //Return the list of replies for each executed command.
                List<Object> results = operations.exec();

                //ToDo: May need to change this to check the result of each command.
                if (results == null) {
                    deleteComment(commentId);
                }
                return null;
            }
        });
    }
}

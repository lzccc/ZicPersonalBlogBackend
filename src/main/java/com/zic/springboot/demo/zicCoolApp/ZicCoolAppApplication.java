package com.zic.springboot.demo.zicCoolApp;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.squareup.okhttp.*;
import com.zic.springboot.demo.zicCoolApp.dao.UserDAO;
import com.zic.springboot.demo.zicCoolApp.entity.User;
import com.zic.springboot.demo.zicCoolApp.services.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.health.HealthEndpointProperties;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

//The scanBasePackages here is optional. Only need to be add if you have other packages outsides of
//the base packages that need to be scanned.
@SpringBootApplication(scanBasePackages = {"com.zic.springboot.demo.zicCoolApp"})
@EnableAspectJAutoProxy
//Since we don't have a mysql server yet, ignore this for now
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
public class ZicCoolAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZicCoolAppApplication.class, args);
	}

	//The run method is the callback method that will be executed when the Spring application starts.
	//You can place your specific code or tasks within this method.
	//The CommandLineRunner bean is automatically detected and executed by the Spring context at startup.
//	@Bean
//	public CommandLineRunner commandLineRunner(UserDAO userDAO, RedisService redisService) {
//		return runner -> {
////			HashMap<String, Object> testhashMap = new HashMap<>();
////			testhashMap.put("test1", "1");
////			testhashMap.put("test2", "2");
////			testhashMap.put("test3", "3");
////			redisService.storeHash("zictesthashKey", testhashMap);
//			//createUser(userDAO);
//			//redisService.createUserKey("createKeytest:test");
//			//System.out.println(redisService.createUserKey("createKeytest:test"));
//		};
//	}

//	@Autowired
//	private void createUser(UserDAO userDAO) {
//		User user = new User("Zichong", "Li", "lizichong@hotmail.com");
//		System.out.println("Saving user..");
//		try{
//			userDAO.save(user);
//		} catch (Exception ex) {
//			System.err.println(ex);
//		}
//		try{
//			User user2 = userDAO.findById(1);
//			System.out.println(user2);
//		} catch (Exception ex) {
//			System.err.println(ex);
//		}
//		try{
//			List<User> user2 = userDAO.findAll();
//			System.out.println(user2);
//		} catch (Exception ex) {
//			System.err.println(ex);
//		}
//		try{
//			List<User> user3 = userDAO.findByLastName("Li");
//			System.out.println(user3);
//		} catch (Exception ex) {
//			System.err.println(ex);
//		}
//		try{
//			User user4 = userDAO.findById(1);
//			user4.setFirstName("Chong");
//			userDAO.update(user4);
//		} catch (Exception ex) {
//			System.err.println(ex);
//		}
//	}
}

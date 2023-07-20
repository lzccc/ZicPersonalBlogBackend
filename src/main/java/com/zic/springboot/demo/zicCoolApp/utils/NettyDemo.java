package com.zic.springboot.demo.zicCoolApp.utils;

//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.SimpleChannelInboundHandler;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//import org.springframework.http.HttpMethod;
//
//import java.net.http.HttpHeaders;
//import java.net.http.HttpRequest;

public class NettyDemo {
//    private static final int port = 8080;
//    public static void main(String[] args) {
//        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
//        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
//        try {
//            ServerBootstrap serverBootstrap = new ServerBootstrap().group(bossGroup, workerGroup)
//                    .channel(NioServerSocketChannel.class)
//                    .childHandler(new ChannelInitializer<SocketChannel>() {
//                        @Override
//                        protected void initChannel(SocketChannel ch) throws Exception {
//                            ch.pipeline().addLast(new HttpRequestDecoder());
//                            ch.pipeline().addLast(new HttpObjectAggregator(65536));
//                            ch.pipeline().addLast(new HttpResponseEncoder());
//                            ch.pipeline().addLast(new SimpleChannelInboundHandler<HttpRequest>() {
//                                @Override
//                                protected void channelRead0(ChannelHandlerContext ctx, HttpRequest request) {
//                                    HttpHeaders headers = request.headers();
//                                    headers.forEach((a) -> {
//                                        System.out.println(a.getKey() + ":" + a.getValue());
//                                    });
//                                    String uri = request.getUri();
//                                    System.out.println(uri);
//                                    if (request.method().equals(HttpMethod.GET)) {
//                                        String xmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
//                                                "<ListAllMyBucketsResult>\n" +
//                                                "   <Buckets>\n" +
//                                                "      <Bucket>\n" +
//                                                "         <CreationDate>12423348712</CreationDate>\n" +
//                                                "         <Name>bucket1</Name>\n" +
//                                                "      </Bucket>\n" +
//                                                "   </Buckets>\n" +
//                                                "   <Owner>\n" +
//                                                "      <DisplayName>string</DisplayName>\n" +
//                                                "      <ID>string</ID>\n" +
//                                                "   </Owner>\n" +
//                                                "</ListAllMyBucketsResult>";
//
//                                        FullHttpResponse response = new DefaultFullHttpResponse(
//                                                HttpVersion.HTTP_1_1,
//                                                HttpResponseStatus.OK,
//                                                Unpooled.copiedBuffer(xmlContent, CharsetUtil.UTF_8)
//                                        );
//
//                                        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "application/xml");
//                                        response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
//
//                                        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
//                                    } else {
//                                        ctx.writeAndFlush(new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.METHOD_NOT_ALLOWED));
//                                    }
//                                }
//                            });
//                        }
//                    });
//            ChannelFuture future = serverBootstrap.bind(port).sync();
//            System.out.println("Server started on port " + port);
//            future.channel().closeFuture().sync();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//        }
//    }
}

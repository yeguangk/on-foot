# Server 创建
 1. 创建 ServerBootstrap
 2. 创建 EventLoopGroup，默认 NioEventLoopGroup，Linux 可以使用：EpollEventLoopGroup
 3. 指定使用的 NIO 传输 Channel：NioServerSocketChannel, Linux 使用：EpollServerSocketChannel
 4. 指定端口设置套接字地址：InetSocketAddress
 5. 设置 Channel 参数: SO_BACKLOG, ALLOCATOR
 6. 设置子 Channel参数，SO_SNDBUF, SO_RCVBUF, ALLOCATOR, WRITE_BUFFER_WATER_MARK, TCP_NODELAY, SO_KEEPALIVE
 7. 添加 ChannelHandler 到子 Channel 的 ChannelPipeline
 8. 异步绑定服务器，调用 sync() 方法阻塞等待直到绑定完成   
 9. 获取 Channel 的 CloseFuture，并且阻塞当前现场直到它完成
 10. 关闭 EventLoopGroup，释放所有的资源
 
            EventLoopGroup bossGroup = getAdaptedGroup(),
                            workerGroup = getAdaptedGroup();
            try {
                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(bossGroup, workerGroup
                         .channel(getAdaptedSocketChannelClass())
                         // 链接排队队列设置为 1024 个
                         .option(ChannelOption.SO_BACKLOG, 1024)
                         .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                         // TCP 发送缓冲区和接收缓冲区都是设置为 16K
                         .childOption(ChannelOption.SO_SNDBUF, 16 * 1024)
                         .childOption(ChannelOption.SO_RCVBUF, 8 * 1024)
                         // 内存池管理
                         .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                         // 写限制
                         .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(16 * 1024, 32 * 1024))
                         .childOption(ChannelOption.TCP_NODELAY, true)
                         .childOption(ChannelOption.SO_KEEPALIVE, true)
                         .childHandler(new ChannelInitializer<SocketChannel>() {
                
                               @Override
                               public void initChannel(SocketChannel ch) {
                
                               SSLEngine engine = sslContext.createSSLEngine();
                               engine.setUseClientMode(false);
                               engine.setNeedClientAuth(false);
              
                               ch.pipeline()
                               .addLast(new SslHandler(engine))
                               .addLast(new IdleStateHandler(heartbeatTimeout, 0, 0, TimeUnit.SECONDS))
                               .addLast(new OStreamCodec.Decoder())
                               .addLast(new OStreamCodec.Encoder())
                               .addLast(new ChannelRegisterHandler(handlerConfig))
                               .addLast(new HeartbeatTimeoutHandler(handlerConfig))
                               .addLast(new ReportReceiveHandler(handlerConfig));
                          }
                });
                ChannelFuture f = bootstrap.bind(port).sync().addListener(
                                    (ChannelFutureListener) future -> {
                                        if (future.isSuccess()) {
                                            LOGGER.info("o-stream started on {}", port);
                                        }
                                    });
                
                f.channel().closeFuture().sync();
            }catch (Exception e) {
            
            }finally{
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            }
         
  
 # Client 
   1. 创建 EventLoopGroup 默认：默认 NioEventLoopGroup，Linux 可以使用：EpollEventLoopGroup
   2. 创建 Bootstrap，绑定 EventLoopGroup
   3. 设置 channle option: SO_KEEPALIVE、SO_SNDBUF、TCP_NODELAY
   4. 添加 ChannelHandler
   5. 异步绑定服务器，调用 sync() 方法阻塞等待直到绑定完成 
   6. 关闭 EventLoopGroup，释放所有的资源

          try {
               eventLoopGroup = configuration.getEventLoopGroupClass().newInstance();
               Bootstrap bootstrap = new Bootstrap();
               bootstrap.group(eventLoopGroup)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_SNDBUF, 8092)
                    .option(ChannelOption.TCP_NODELAY, true);

               bootstrap.attr(Constant.SN_ATTR, sn);

               // 关联 ChannelHandler
               bootstrap.channel(configuration.getSocketChannelClass())
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(sslCtx.newHandler(ch.alloc(),
                                            configuration.getNettyServer().getHost(),
                                            configuration.getNettyServer().getPort()))
                                    .addLast(new OStreamCodec.Decoder())
                                    .addLast(new OStreamCodec.Encoder())
                                    .addLast(new IdleStateHandler(0,
                                            configuration.getHeartbeatInterval(), 0, TimeUnit.SECONDS))
                                    .addLast(new HeartbeatChannelHandler(configuration.isSendHeartbeat()))
                                    .addLast(new CommandHandler(commandService));
                        }
                    });

               ChannelFuture f = bootstrap.connect(configuration.getNettyServer().getHost(),
                    configuration.getNettyServer().getPort());

               f.sync();
               f.channel().closeFuture().sync();

            }catch (Exception e) {
              if (Objects.nonNull(eventLoopGroup)) {
                  eventLoopGroup.shutdownGracefully();
                  // 通知 GC
                  eventLoopGroup = null;
               }
            } finally {

              if (Objects.nonNull(eventLoopGroup)) {
                  eventLoopGroup.shutdownGracefully();
              }
           }
   
   注：客户端一般会使用 SimpleChannelInboundHandler，在 SimpleChannelInboundHandler 中当 channelRead0() 方法完成时，
   SimpleChannelInboundHandler 将负载释放指向 
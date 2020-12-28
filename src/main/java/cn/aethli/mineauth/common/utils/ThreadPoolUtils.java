//package cn.aethli.mineauth.common.utils;
//
//import cn.aethli.mineauth.config.MineauthConfig;
//
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.SynchronousQueue;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
///**
// * utils for thread pool
// *
// * <p>database operations aren't sync, for reduce database response time block, register; login;
// * changePassword are designed to async operation;
// *
// * @author selcaNyan
// */
//public class ThreadPoolUtils {
//  private static final ExecutorService cachedThreadPool;
//
//  // init thread pool
//  static {
//    cachedThreadPool =
//        new ThreadPoolExecutor(
//            MineauthConfig.databaseConfig.poolSize.get(),
//            20,
//            MineauthConfig.databaseConfig.timeout.get(),
//            TimeUnit.SECONDS,
//            new SynchronousQueue<>());
//  }
//
//  public static ExecutorService getCachedThreadPool() {
//    return cachedThreadPool;
//  }
//}

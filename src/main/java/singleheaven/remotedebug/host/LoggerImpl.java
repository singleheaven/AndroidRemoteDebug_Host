package singleheaven.remotedebug.host;

import jdi.log.Log;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;

@Named("loggerImpl")
@Slf4j
public class LoggerImpl implements Log.ILog {
    @Override
    public void e(String s, String s1, Throwable throwable) {
//        log.error(String.format("%s:%s", s, s1), throwable);
        System.out.println(String.format("error %s:%s %s", s, s1, throwable.getMessage()));
    }

    @Override
    public void d(String s, String s1) {
//        log.debug(String.format("%s:%s", s, s1));
        System.out.println(String.format("%s:%s", s, s1));
    }

    @Override
    public void e(String s, String s1) {
//        log.error(String.format("%s:%s", s, s1));
        System.out.println(String.format("%s:%s", s, s1));
    }
}

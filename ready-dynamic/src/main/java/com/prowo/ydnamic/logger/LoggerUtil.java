package com.prowo.ydnamic.logger;

import com.prowo.ydnamic.validation.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.MessageFormat;

/**
 * 日志操作类
 */
public final class LoggerUtil {
    final static Log logger = LogFactory.getLog(Object.class);

    public enum Level {
        DEBUG, INFO, WRAN, WARN, ERROR;
    }

    public static void log(Level level, String message, Object... params) {
        if (Validate.isNull(message)) {
            message = "{0}";
        }
        String paramObject = MessageFormat.format(message, params);
        switch (level) {
            case DEBUG:
                if (logger.isDebugEnabled())
                    logger.debug(paramObject);
                break;
            case INFO:
                if (logger.isInfoEnabled())
                    logger.info(paramObject);
                break;
            case WARN:
            case WRAN:
                if (logger.isWarnEnabled())
                    logger.warn(paramObject);
                break;
            case ERROR:
                if (logger.isErrorEnabled())
                    logger.error(paramObject);
                break;
            default:
                break;
        }

    }

    public static void log(Level level, Throwable paramThrowable, String message, Object... params) {
        if (Validate.isNull(message)) {
            message = "{0}";
        }
        String paramObject = MessageFormat.format(message, params);
        switch (level) {
            case DEBUG:
                if (logger.isDebugEnabled())
                    logger.debug(paramObject, paramThrowable);
                break;
            case INFO:
                if (logger.isInfoEnabled())
                    logger.info(paramObject, paramThrowable);
                break;
            case WARN:
            case WRAN:
                if (logger.isWarnEnabled())
                    logger.warn(paramObject, paramThrowable);
                break;
            case ERROR:
                if (logger.isErrorEnabled())
                    logger.error(paramObject, paramThrowable);
                break;
            default:
                break;
        }
    }

}

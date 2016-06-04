package fr.xgouchet.packageexplorer.aspects;


import android.os.SystemClock;
import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.text.NumberFormat;

import static fr.xgouchet.packageexplorer.aspects.LogUtils.appendArgs;
import static fr.xgouchet.packageexplorer.aspects.LogUtils.appendMethodName;
import static fr.xgouchet.packageexplorer.aspects.LogUtils.getTag;

/**
 * @author Xavier Gouchet
 */
@Aspect
public class MonitorMeAspect {


    @Pointcut("execution(@fr.xgouchet.packageexplorer.annotations.MonitorMe *.new(..))")
    public void annotatedConstructor() {
    }

    @Pointcut("execution(@fr.xgouchet.packageexplorer.annotations.MonitorMe * *(..))")
    public void annotatedMethod() {
    }

    @Around("annotatedMethod() || annotatedConstructor()")
    public Object adviceAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        long startNano, endNano;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            startNano = SystemClock.elapsedRealtimeNanos();
        } else {
            startNano = System.nanoTime();
        }
        Object result = proceedingJoinPoint.proceed();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            endNano = SystemClock.elapsedRealtimeNanos();
        } else {
            endNano = System.nanoTime();
        }

        StringBuilder message = new StringBuilder();
        message.append("⌚ ");

        appendMethodName(proceedingJoinPoint, message);
        appendArgs(proceedingJoinPoint, message);
        message.append(" took ")
                .append(NumberFormat.getIntegerInstance().format(endNano - startNano))
                .append(" nanos");

        Log.i(getTag(proceedingJoinPoint), message.toString());

        return result;
    }
}
package com.malfaang.e_culture_tool_a.auth.data;

import androidx.annotation.NonNull;

/**
 * A generic class that holds a result success w/ data or an error exception.
 */
public class Result<T> {
    // hide the private constructor to limit subclass types (Success, Error)
    Result() {
    }

    public Result(Exception errorLoggingIn) {

    }

    @NonNull
    @Override
    public String toString() {
        if (this instanceof Result.Success) {
            return "Success[data=" + ((Success<T>) this).getData().toString() + "]";
        } else if (this instanceof Result.Error) {
            return "Error[exception=" + ((Error) this).getError().toString() + "]";
        }else{
            return "";
        }
    }

    // Success sub-class
    public static final class Success<T> extends Result<T> {
        private final T data;

        public Success(T data) {
            this.data = data;
        }

        public T getData() {
            return this.data;
        }
    }

    // Error sub-class
    public final class Error extends Result<T> {
        private final Exception error2;

        public Error(Exception error) {
            this.error2 = error;
        }

        public Exception getError() {
            return this.error2;
        }
    }
}

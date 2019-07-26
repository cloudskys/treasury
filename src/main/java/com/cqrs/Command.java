package com.cqrs;

public interface Command<T> {
	Object execute(T commandModel);
}

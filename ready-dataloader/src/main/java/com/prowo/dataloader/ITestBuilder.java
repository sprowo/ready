package com.prowo.dataloader;

import java.util.regex.Pattern;

public interface ITestBuilder {
	Pattern getPattern();

	String getDefaultPath();
}

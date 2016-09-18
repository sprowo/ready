package com.prowo.dataloader;

import java.io.IOException;

public interface ITextLoader {
	String getTestDataById(String id);

	void initData() throws IOException;

	void initData(String path) throws IOException;
}

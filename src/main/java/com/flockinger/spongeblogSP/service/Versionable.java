package com.flockinger.spongeblogSP.service;

import com.flockinger.spongeblogSP.exception.NoVersionFoundException;

public interface Versionable {
	void rewind(Long id) throws NoVersionFoundException;
}

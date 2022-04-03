package com.berge.ratenow.testapplication.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class CustomPageRequest extends PageRequest {

	private String sortDirString;
	private String sortString;

	public static CustomPageRequest of(int page, int size) {
		return of(page, size, Sort.unsorted());
	}

	public static CustomPageRequest of(int page, int size, Sort sort) {
		return new CustomPageRequest(page, size, sort);
	}

	@Deprecated
	public CustomPageRequest(int page, int size, Sort sort) {

		super(page, size, null);
	}

	public CustomPageRequest(int page, int size) {
		super(page, size, null);
	}

	public String getSortDirString() {
		return sortDirString;
	}

	public void setSortDirString(String sortDirString) {
		this.sortDirString = sortDirString;
	}

	public String getSortString() {
		return sortString;
	}

	public void setSortString(String sortString) {
		this.sortString = sortString;
	}
}

package com.flockinger.spongeblogSP.dto;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class Paging implements Pageable{
	private int pageNumber;
	private int pageSize;
	private Sort sort;
	
	public Paging(){}
	
	public Paging(int pageNumber, Sort sort, int pageSize){
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.sort = sort;
	}
	
	@Override
	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	
	@Override
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	@Override
	public Sort getSort() {
		return sort;
	}

	public void setSort(Sort sort) {
		this.sort = sort;
	}

	@Override
	public int getOffset() {
		return pageNumber * pageSize;
	}

	@Override
	public Pageable next() {
		return null;
	}

	@Override
	public Pageable previousOrFirst() {
		return null;
	}

	@Override
	public Pageable first() {
		return null;
	}

	@Override
	public boolean hasPrevious() {
		return false;
	}
}

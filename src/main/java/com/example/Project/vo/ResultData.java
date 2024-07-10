package com.example.Project.vo;

import lombok.Getter;

public class ResultData<DT> {
	@Getter
	private String resultCode;
	@Getter
	private String resultMsg;
	@Getter
	private DT data;

	public static <DT> ResultData<DT> from(String resultCode, String resultMsg) {
		return from(resultCode, resultMsg, null);
	}

	public static <DT> ResultData<DT> from(String resultCode, String resultMsg, DT data) {
		
		ResultData<DT> result = new ResultData<>();
		
		result.resultCode = resultCode;
		result.resultMsg = resultMsg;
		result.data = data;
		
		return result;
	}
	
	public boolean isSuccess() {
		return this.resultCode.startsWith("S-");
	}
	
	public boolean isFail() {
		return isSuccess() == false;
	}
	
}
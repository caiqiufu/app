package com.slipper.unieap.metaapi.mt4;

import java.io.IOException;

import cloud.metaapi.sdk.meta_api.MetaApi;
import cloud.metaapi.sdk.meta_api.MetatraderAccount;

public class MT4Demo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String token = "...";
		try {
			MetaApi api = new MetaApi(token);

			// Account access token grants access to a single account. You can retrieve
			// account access token via API:
			String accountId = "...";
			MetatraderAccount account = api.getMetatraderAccountApi().getAccount(accountId).join();
			String accountAccessToken = account.getAccessToken();
			System.out.println(accountAccessToken);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

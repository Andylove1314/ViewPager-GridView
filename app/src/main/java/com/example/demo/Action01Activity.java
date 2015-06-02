package com.example.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Action01Activity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		Intent in = getIntent();
		setTitle(in.getExtras().getString("action"));
	}
}

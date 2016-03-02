package com.filmresource.cn.net.manager;


/**
 * Abstract LoaderControler that implements LoadControler
 * 
 */
public  class AbsLoadControler implements LoadControler {

	@Override
	public void cancel() {
		}

	protected String getOriginUrl() {
		return "";//this.mRequest.getOriginUrl();
	}
}
package com.soin.sgrm.service;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;

public interface EmailReadService {
	void emailRead() throws MessagingException;
}

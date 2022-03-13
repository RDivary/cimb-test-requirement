package com.divary.cimbtestrequirement.service;

import com.divary.cimbtestrequirement.model.User;

public interface ExcelGenerator {
    byte[] generateTransactionHistory(User user);
}

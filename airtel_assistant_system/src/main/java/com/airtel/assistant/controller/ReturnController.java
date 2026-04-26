package com.airtel.assistant.controller;

import com.airtel.assistant.service.ReturnService;

public class ReturnController {

    private ReturnService service = new ReturnService();

    public boolean returnAsset(int assetId, String date, String condition) {
        return service.returnAsset(assetId, date, condition);
    }
}
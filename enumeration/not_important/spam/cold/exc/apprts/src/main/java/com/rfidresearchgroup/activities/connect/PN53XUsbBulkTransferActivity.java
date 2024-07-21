package com.rfidresearchgroup.activities.connect;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.rfidresearchgroup.models.AbstractDeviceModel;
import com.rfidresearchgroup.models.UniversalBulkPN53XRawModel;

import com.rfidresearchgroup.devices.PN53X;

public class PN53XUsbBulkTransferActivity extends AbstractPN53XConnectActivity {

    private String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 先接收参数，判断我们需要打开什么设备？
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        if (name == null) name = PN53X.NAME.UNKNOWN;
        // 调用父类的构建过程来构建！
        super.onCreate(savedInstanceState);
    }

    @Override
    public AbstractDeviceModel[] getModels() {
        return new AbstractDeviceModel[]{
                new UniversalBulkPN53XRawModel(name)
        };
    }
}

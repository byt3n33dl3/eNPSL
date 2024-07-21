package com.rfidresearchgroup.models;

import android.nfc.Tag;
import android.nfc.tech.MifareClassic;

import java.io.IOException;

import com.rfidresearchgroup.common.util.IOUtils;
import com.rfidresearchgroup.mifare.NfcTagListenUtils;

public class StandardNFCTagStateModel extends AbsTagStateModel {
    @Override
    protected boolean checkTagState() {
        //判断标准设备MF是否存在实例引用
        Tag tag = NfcTagListenUtils.getTag();
        // 直接判断全局的标签状态是否正常!
        if (tag != null) {
            //判断能否链接!
            MifareClassic mfTag = MifareClassic.get(tag);
            // 直接判断全局的标签状态是否正常!
            if (mfTag == null) return false;
            try {
                mfTag.connect();
                mfTag.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.close(mfTag);
            }
        }
        return false;
    }

    @Override
    protected boolean checkTagMagic() {
        // 内置NFC不支持特殊后门卡读写，因此直接返回false!
        return false;
    }
}

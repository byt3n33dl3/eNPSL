package com.rfidresearchgroup.activities.proxmark3.rdv4_rrg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;

import com.rfidresearchgroup.activities.main.BaseActivity;
import com.rfidresearchgroup.activities.main.PM3FlasherMainActivity;
import com.rfidresearchgroup.rfidtools.R;
import com.rfidresearchgroup.util.Commons;
import com.rfidresearchgroup.util.Proxmark3Installer;

import com.termux.app.TermuxActivity;

import com.rfidresearchgroup.common.widget.ToastUtil;

public class Proxmark3NewTerminalInitActivity extends BaseActivity {

    private RadioGroup rdoGroupTerminalSelect;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pm3_terminal_init);

        rdoGroupTerminalSelect = findViewById(R.id.rdoGroupTerminalSelect);
        rdoGroupTerminalSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdoBtnFullTerminal:
                        Commons.setTerminalType(0);
                        break;
                    case R.id.rdoBtnSimpleTerminal:
                        Commons.setTerminalType(1);
                        break;
                }
            }
        });
        rdoGroupTerminalSelect.check(checkTerminalType());

        findViewById(R.id.btnFwFlash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, PM3FlasherMainActivity.class));
            }
        });

        findViewById(R.id.btnGoToTermux).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rdoGroupTerminalSelect.getCheckedRadioButtonId() == -1) {
                    ToastUtil.show(context, getString(R.string.tips_terminal_select_like), false);
                    return;
                }
                if (Commons.isPM3ClientDecompressed()) {
                    go();
                } else {
                    Proxmark3Installer.installIfNeed(activity, new Runnable() {
                        @Override
                        public void run() {
                            go();
                        }
                    });
                }
            }
        });

        CheckBox box = findViewById(R.id.ckBoxAutoGoTermux);
        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Commons.setAutoGoToTerminal(isChecked);
            }
        });
        box.setChecked(Commons.getAutoGoToTerminal());

        // Must have init pm3 client and check auto go and have the terminal type selected!
        if (Commons.getAutoGoToTerminal() && Commons.getTerminalType() != -1) {
            if (Proxmark3Installer.isCanInstall(activity)) {
                Proxmark3Installer.installIfNeed(activity, this::go);
            } else {
                go();
            }
        }
    }

    public void go() {
        if (Commons.getTerminalType() == 0) {
            startActivity(new Intent(this, TermuxActivity.class));
        } else {
            startActivity(new Intent(this, Proxmark3Rdv4RRGConsoleActivity.class));
        }
        finish();
    }

    private int checkTerminalType() {
        switch (Commons.getTerminalType()) {
            case -1:
            default:
                return -1;

            case 0:
                return R.id.rdoBtnFullTerminal;

            case 1:
                return R.id.rdoBtnSimpleTerminal;
        }
    }
}

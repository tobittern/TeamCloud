package com.euler.system.api;

import com.euler.common.mybatis.core.page.TableDataInfo;

public interface TestDubbo {
     String getTestStr() ;

   TableDataInfo<String> getsPageed();
    }


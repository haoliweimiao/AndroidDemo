//
// Created by 毫厘微 on 2020/5/24.
//

#import "OtherUtil.h"

long long currentTimeInMilliseconds() {
    struct timeval tv;
    gettimeofday(&tv, NULL);
    return ((tv.tv_sec * 1000) + (tv.tv_usec / 1000));
}
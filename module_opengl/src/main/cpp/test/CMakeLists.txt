cmake_minimum_required(VERSION 3.4.1)

AUX_SOURCE_DIRECTORY(. TEST_DIR_SRC)
ADD_LIBRARY(libtest SHARED ${TEST_DIR_SRC})


#add_library(libtest SHARED test.cpp)
##依赖库
find_library(log-lib log)
##
target_link_libraries(libtest ${log-lib})
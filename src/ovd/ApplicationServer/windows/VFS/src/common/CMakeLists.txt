file(GLOB_RECURSE COMMON_FILE *.cpp)

add_library(common ${COMMON_FILE})
target_link_libraries(common Shlwapi)

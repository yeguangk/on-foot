# SQL 调用
- MAX 函数检查表中是否至少存在一行记录，推荐使用 MAX 计数，因为 InnoDB 表不会保存记录的行数，而是在每次调用 
COUNT 函数的时候进行现计算，因此 MAX(index_field) 函数比 COUNT 快

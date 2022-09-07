package com.qc.boot.elastic;

import com.qc.boot.entity.Students;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * 功能描述:
 *
 * @author lijinhua
 * @date 2022/9/7 15:39
 */
@Repository
/**这点和mp贼像，那么实际上mp搭配dao，写起来也不错
 * StudentRepository在实现层去使用
 * */
public interface StudentsRepository extends ElasticsearchRepository<Students, String> {
}

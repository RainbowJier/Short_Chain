package service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import .dao.TrafficTaskDao;
import .entity.TrafficTask;
import .service.TrafficTaskService;
import org.springframework.stereotype.Service;

/**
 * (TrafficTask)表服务实现类
 *
 * @author RainbowJier
 * @since 2024-12-29 14:37:32
 */
@Service("trafficTaskService")
public class TrafficTaskServiceImpl extends ServiceImpl<TrafficTaskDao, TrafficTask> implements TrafficTaskService {

}


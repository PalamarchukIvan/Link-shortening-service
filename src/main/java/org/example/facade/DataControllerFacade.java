package org.example.facade;

import lombok.RequiredArgsConstructor;
import org.example.dto.DataEntityResponseDto;
import org.example.dto.GetStatisticsDto;
import org.example.model.User;
import org.example.service.DataService;
import org.example.util.CurrentUserUtil;
import org.example.util.Mapstruct.DataMapper;
import org.example.web.ResultWithStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DataControllerFacade {

    private final DataService dataService;

    public ResultWithStatus<List<DataEntityResponseDto>> getStats(GetStatisticsDto request) {
        User user = CurrentUserUtil.getCurrentUser();
        request.setUser(user);
        return ResultWithStatus.ok(DataMapper.INSTANCE.toDto(dataService.getFiltered(request)));
    }

    public ResultWithStatus<List<DataEntityResponseDto>> getStats() {
        return ResultWithStatus.ok(DataMapper.INSTANCE.toDto(dataService.getFiltered(GetStatisticsDto.EMPTY)));
    }

}

package com.kafka.validation;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class RecordValidator {

    public ValidationResult validate(List<ConsumerRecord> backupConsumerRecords, List<ConsumerRecord> sourceConsumerRecords) {
        if (backupConsumerRecords.size() > sourceConsumerRecords.size()){
            return ValidationResult.INCONSISTENT_PARTITION_SIZE;
        }

        if (CollectionUtils.isEmpty(backupConsumerRecords)){
            return ValidationResult.SUCCESSFUL;
        }

        for (int i = 0; i < backupConsumerRecords.size(); i++) {
            if (!isRecordSame(backupConsumerRecords.get(i), sourceConsumerRecords.get(i))){
                return ValidationResult.DEFECT_DATA;
            }
        }
        return ValidationResult.SUCCESSFUL_BUFFER_PORTION;
    }

    private boolean isRecordSame(ConsumerRecord backupRecord, ConsumerRecord sourceRecord) {
        return Arrays.equals((byte[]) backupRecord.value(), (byte[]) sourceRecord.value()) && Arrays.equals((byte[]) backupRecord.key(), (byte[]) sourceRecord.key());
    }
}

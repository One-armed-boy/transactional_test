### 실행
```shell
docker-compose up -d
```
```shell
./gradlew clean test
```
### 예상 결과

- 두 테스트 케이스 중 UserServiceWithoutTx 관련 테스트는 실패
- UserServiceWithTx 케이스는 성공
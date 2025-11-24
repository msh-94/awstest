import { useState } from "react";
import axios from "axios";

function TestPage() {

  const [restResult, setRestResult] = useState("");
  const [rdsResult, setRdsResult] = useState([]);
  const [cacheResult, setCacheResult] = useState([]);
  const [s3Result, setS3Result] = useState("");

  // 1. 일반 REST
  const callRest = async () => {
    const res = await axios.get("/api/rest");
    setRestResult(res.data);
  };

  // 2. DB (RDS)
  const callRds = async () => {
    const content = prompt("전송할 content?");
    if (!content) return;

    const res = await axios.post(`/api/rds?content=${content}`);
    setRdsResult(res.data);
  };

  // 3. Redis 캐싱
  const callCache = async () => {
    const content = prompt("캐싱 content?");
    if (!content) return;

    const res = await axios.post(`/api/cache?content=${content}`);
    setCacheResult(res.data);
  };

  // 4. S3 파일 업로드
  const callS3 = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append("file", file);

    const res = await axios.post("/api/s3", formData, {
      headers: { "Content-Type": "multipart/form-data" }
    });

    setS3Result(res.data);
  };

  return (
    <div>
      <h2>Test Page</h2>

      {/* 1. 일반 REST */}
      <div>
        <button onClick={callRest}>1. 일반 REST 호출</button>
        {restResult && <p>결과: {restResult}</p>}
      </div>

      <hr />

      {/* 2. RDS */}
      <div>
        <button onClick={callRds}>2. RDS 테스트 호출</button>
        {rdsResult.length > 0 && (
          <div style={{ marginTop: "10px" }}>
            <h4>RDS 결과</h4>
            <pre>{JSON.stringify(rdsResult, null, 2)}</pre>
          </div>
        )}
      </div>

      <hr />

      {/* 3. Redis 캐싱 */}
      <div>
        <button onClick={callCache}>3. Redis 캐싱 호출</button>
        {cacheResult.length > 0 && (
          <div style={{ marginTop: "10px" }}>
            <h4>Redis 결과</h4>
            <pre>{JSON.stringify(cacheResult, null, 2)}</pre>
          </div>
        )}
      </div>

      <hr />

      {/* 4. S3 업로드 */}
      <div>
        <p>4. S3 파일 업로드</p>
        <input type="file" onChange={callS3} />

        {s3Result && (
          <div style={{ marginTop: "10px" }}>
            <h4>S3 업로드 성공 결과</h4>

            {/* S3에서 반환하는 URL 이라면 이미지도 표시 */}
            {s3Result.startsWith("http") ? (
              <div>
                <img src={s3Result} alt="uploaded" width={250} />
                <p>{s3Result}</p>
              </div>
            ) : (
              <p>{s3Result}</p>
            )}
          </div>
        )}
      </div>

    </div>
  );
}

export default TestPage;

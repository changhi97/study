import cv2
import dlib

# 얼굴 검출기 초기화
detector = dlib.get_frontal_face_detector()

# 랜드마크 예측 모델 경로
predictor_path = "shape_predictor_68_face_landmarks.dat"  # 예측 모델 파일의 경로를 지정해야 합니다.

# 얼굴 랜드마크 예측 모델 로드
predictor = dlib.shape_predictor(predictor_path)

# 얼굴 영역 내의 입술 좌표 추출 함수
def extract_lip_points(shape):
    lip_points = []
    for i in range(48, 68):  # 입술 영역의 랜드마크 인덱스
        x = shape.part(i).x
        y = shape.part(i).y
        lip_points.append((x, y))
    return lip_points

# 얼굴 영역 내의 턱 좌표 추출 함수
def extract_chin_points(shape):
    chin_points = []
    for i in range(0, 17):  # 턱 영역의 랜드마크 인덱스
        x = shape.part(i).x
        y = shape.part(i).y
        chin_points.append((x, y))
    return chin_points

# 웹캠 비디오 스트림 열기
cap = cv2.VideoCapture(0)

# 해상도 조정
cap.set(cv2.CAP_PROP_FRAME_WIDTH, 640)
cap.set(cv2.CAP_PROP_FRAME_HEIGHT, 480)

frame_skip = 5  # 프레임 스킵 횟수
skip_count = 0

while cap.isOpened():
    ret, frame = cap.read()
    if not ret:
        break

    skip_count += 1
    if skip_count < frame_skip:
        continue
    skip_count = 0

    # 그레이스케일로 변환
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    # 얼굴 검출 (프레임 스킵 후에만 수행)
    if skip_count == 0:
        faces = detector(gray)
    else:
        faces = []

    for face in faces:
        # 얼굴 영역의 랜드마크 추출
        landmarks = predictor(gray, face)

        # 입술 좌표 추출
        lip_points = extract_lip_points(landmarks)

        # 턱 좌표 추출
        chin_points = extract_chin_points(landmarks)

        # 입술 및 턱 좌표를 이용하여 아, 에, 이, 오, 우 판별
        # 입술의 평균 y좌표를 계산하여 아/오 판별
        lip_avg_y = sum(point[1] for point in lip_points) / len(lip_points)

        # 턱의 중심 좌표를 계산하여 에/이/우 판별
        chin_center_x = (chin_points[8][0] + chin_points[9][0]) // 2
        chin_center_y = (chin_points[8][1] + chin_points[9][1]) // 2

        # 아/오 판별
        if lip_avg_y > chin_center_y:
            cv2.putText(frame, "a", (50, 50), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)
        else:
            cv2.putText(frame, "o", (50, 50), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)

        # 에/이/우 판별
        if lip_avg_y > chin_points[0][1]:
            cv2.putText(frame, "e", (50, 100), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)
        elif lip_points[0][1] - chin_points[1][1] > 5:  # 조정된 값: 5
            cv2.putText(frame, "i", (50, 100), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)
        else:
            cv2.putText(frame, "u", (50, 100), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)

        # 입술 및 턱 좌표에 점 그리기
        for point in lip_points:
            cv2.circle(frame, point, 2, (0, 255, 0), -1)
        for point in chin_points:
            cv2.circle(frame, point, 2, (0, 0, 255), -1)

    # 결과 표시
    cv2.imshow("Webcam", frame)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

# 정리
cap.release()
cv2.destroyAllWindows()

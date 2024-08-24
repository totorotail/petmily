-- 회원 테이블
insert into users (username, email, password, address, age, birth_date,
                   gender, name, nickname, phone, role, user_status, is_walking,
                   registered_at, modified_at)
values
    -- USER_ID 1
    ('kimjun91', 'kim91.jun@example.com',
     '$2a$10$V5HITwwdQLm673Q/gg.gk.I6.BLKuLRXis6/UyK.RA6nnhXHArsre', '서울시 강남구',
     33, '1994-01-10',
     'M', '김준', '사과', '010-1111-2222', 'USER', 'ACTIVE', FALSE,
     '2024-08-10T14:30:15', '2024-08-10T14:30:15'),
-- USER_ID 2
    ('leesora96', 'lee.sora@example.com',
     '$2a$10$iAEySf9Bhan3CE//5uz3DudIL7YAmtkUwl9niY9IP7UP6jACYTx..', '서울시 마포구',
     28, '1996-05-20',
     'F', '이소라', '바나나', '010-3333-4444', 'USER', 'ACTIVE', FALSE,
     '2024-08-11T09:15:30', '2024-08-11T09:15:30'),
-- USER_ID 3
    ('parkhyun89', 'park.hyun@example.com',
     '$2a$10$HvapuGDKf5S7LfHhkK.KUOeckZFIKgLzGyKc6O/RsOxZYruqnTYaa', '서울시 송파구',
     35, '1989-12-05',
     'M', '박현', '체리', '010-5555-6666', 'USER', 'ACTIVE', FALSE,
     '2024-08-12T11:45:00', '2024-08-12T11:45:00'),
-- USER_ID 4
    ('choiminji02', 'choi.minji@example.com',
     '$2a$10$Kwx87YVXeMISffvXq7Dg1.JMppxMTpj4ytuLt3hpcOmYcqGrO9yyy', '서울시 용산구',
     22, '2002-08-15',
     'F', '최민지', '오렌지', '010-7777-8888', 'USER', 'ACTIVE', FALSE,
     '2024-08-13T16:30:00', '2024-08-13T16:30:00'),
-- USER_ID 5
    ('jungeun97', 'jung.eun@example.com',
     '$2a$10$BFWt7Ohw3EaFMQ2j0QoBF.oPDyRC4MZQH85PcTXqiJPvCE5eTtj8u', '서울시 중구',
     27, '1997-03-30',
     'F', '정은', '포도', '010-9999-0000', 'USER', 'ACTIVE', FALSE,
     '2024-08-14T08:00:00', '2024-08-14T08:00:00');

-- 반려동물 테이블
INSERT INTO pet (USER_ID, CATEGORY, BRAND, BIRTH_DATE, AGE, NAME, GENDER,
                 IS_PETS_NEUTER, WEIGHT, PET_STATUS, CHIP, CREATE_AT,
                 MODIFIED_AT)
VALUES
    -- USER_ID 1의 반려동물
    (1, 'DOG', 'Labrador Retriever', '2020-05-15', 3, 'Max', 'MALE', true, 30.5,
     'ACTIVE', 'CHIP123456', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (1, 'CAT', 'Persian', '2021-03-10', 2, 'Luna', 'FEMALE', true, 4.2,
     'ACTIVE', 'CHIP234567', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (1, 'DOG', 'Golden Retriever', '2019-11-20', 4, 'Charlie', 'MALE', false,
     28.7, 'ACTIVE', 'CHIP345678', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- USER_ID 2의 반려동물
    (2, 'CAT', 'Siamese', '2022-01-05', 1, 'Milo', 'MALE', false, 3.8, 'ACTIVE',
     'CHIP456789', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'DOG', 'Poodle', '2020-09-30', 3, 'Bella', 'FEMALE', true, 15.3,
     'ACTIVE', 'CHIP567890', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'CAT', 'Maine Coon', '2021-07-12', 2, 'Oliver', 'MALE', true, 5.5,
     'ACTIVE', 'CHIP678901', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- USER_ID 3의 반려동물
    (3, 'DOG', 'German Shepherd', '2019-04-22', 4, 'Rocky', 'MALE', true, 35.2,
     'ACTIVE', 'CHIP789012', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, 'CAT', 'British Shorthair', '2020-12-03', 2, 'Lucy', 'FEMALE', false,
     4.0, 'ACTIVE', 'CHIP890123', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, 'DOG', 'Beagle', '2021-08-17', 2, 'Daisy', 'FEMALE', true, 12.8,
     'ACTIVE', 'CHIP901234', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- USER_ID 4의 반려동물
    (4, 'DOG', 'Shiba Inu', '2021-02-14', 2, 'Hachi', 'MALE', false, 10.5,
     'ACTIVE', 'CHIP012345', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- USER_ID 5의 반려동물
    (5, 'DOG', 'Corgi', '2020-07-30', 3, 'Coco', 'FEMALE', true, 12.3, 'ACTIVE',
     'CHIP123450', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 산책 테이블
INSERT INTO walk (user_id, walk_status, distance, duration, start_time,
                  end_time, walk_date)
VALUES
    -- User 1
    (1, 1, 3.0, 30.0, '2024-08-10 01:30:00', '2024-08-10 02:00:00',
     '2024-08-10'),
    (1, 1, 2.5, 23.0, '2024-08-11 01:22:00', '2024-08-11 01:45:00',
     '2024-08-11'),
    (1, 1, 4.0, 40.0, '2024-08-12 02:00:00', '2024-08-12 02:40:00',
     '2024-08-12'),

    -- User 2
    (2, 1, 3.5, 35.0, '2024-08-10 03:00:00', '2024-08-10 03:35:00',
     '2024-08-10'),
    (2, 1, 2.0, 20.0, '2024-08-11 03:30:00', '2024-08-11 03:50:00',
     '2024-08-11'),
    (2, 1, 5.0, 50.0, '2024-08-12 04:00:00', '2024-08-12 04:50:00',
     '2024-08-12'),
    (2, 1, 4.5, 45.0, '2024-08-13 04:30:00', '2024-08-13 05:15:00',
     '2024-08-13'),

    -- User 3
    (3, 1, 3.2, 32.0, '2024-08-10 05:00:00', '2024-08-10 05:32:00',
     '2024-08-10'),
    (3, 1, 2.8, 28.0, '2024-08-11 05:30:00', '2024-08-11 05:58:00',
     '2024-08-11'),
    (3, 1, 4.1, 41.0, '2024-08-12 06:00:00', '2024-08-12 06:41:00',
     '2024-08-12'),

    -- User 4
    (4, 1, 3.3, 33.0, '2024-08-10 06:30:00', '2024-08-10 07:03:00',
     '2024-08-10'),
    (4, 1, 2.7, 27.0, '2024-08-11 07:00:00', '2024-08-11 07:27:00',
     '2024-08-11'),
    (4, 1, 4.2, 42.0, '2024-08-12 07:30:00', '2024-08-12 08:12:00',
     '2024-08-12'),
    (4, 1, 5.1, 51.0, '2024-08-13 08:00:00', '2024-08-13 08:51:00',
     '2024-08-13'),
    (4, 1, 3.8, 38.0, '2024-08-14 08:30:00', '2024-08-14 09:08:00',
     '2024-08-14'),

    -- User 5
    (5, 1, 3.4, 34.0, '2024-08-10 09:00:00', '2024-08-10 09:34:00',
     '2024-08-10'),
    (5, 1, 2.6, 26.0, '2024-08-11 09:30:00', '2024-08-11 09:56:00',
     '2024-08-11'),
    (5, 1, 4.3, 43.0, '2024-08-12 10:00:00', '2024-08-12 10:43:00',
     '2024-08-12');

-- 산책 참여 반려동물 테이블
INSERT INTO WALKING_PET (PET_ID, WALK_ID)
VALUES (1, 1),
       (2, 1),
       (1, 2),
       (3, 2),
       (2, 3),
       (3, 3),
       (4, 4),
       (5, 4),
       (5, 5),
       (6, 5),
       (4, 6),
       (6, 6),
       (5, 7),
       (6, 7),
       (7, 8),
       (8, 8),
       (8, 9),
       (9, 9),
       (7, 10),
       (9, 10),
       (10, 11),
       (10, 12),
       (10, 13),
       (10, 14),
       (10, 15),
       (11, 16),
       (11, 17),
       (11, 18);

-- 산책에 대한 산책 활동 테이블
INSERT INTO WALK_ACTIVITY (LATITUDE, LONGITUDE, PET_ID, WALK_ID, TIMESTAMP,
                           ACTIVITY_TYPE)
VALUES (37.5665, 126.9780, 1, 1, '2024-08-10 01:45:00', 'PEE'),
       (37.5666, 126.9781, 2, 1, '2024-08-10 01:55:00', 'POOP'),
       (37.5667, 126.9782, 1, 2, '2024-08-11 01:30:00', 'WATER'),
       (37.5668, 126.9783, 3, 2, '2024-08-11 01:40:00', 'PEE'),
       (37.5669, 126.9784, 2, 3, '2024-08-12 02:20:00', 'POOP'),
       (37.5670, 126.9785, 3, 3, '2024-08-12 02:30:00', 'WATER'),
       (37.5671, 126.9786, 4, 4, '2024-08-10 03:15:00', 'PEE'),
       (37.5672, 126.9787, 5, 4, '2024-08-10 03:25:00', 'POOP'),
       (37.5673, 126.9788, 5, 5, '2024-08-11 03:40:00', 'WATER'),
       (37.5674, 126.9789, 6, 5, '2024-08-11 03:50:00', 'PEE'),
       (37.5675, 126.9790, 4, 6, '2024-08-12 04:25:00', 'POOP'),
       (37.5676, 126.9791, 6, 6, '2024-08-12 04:35:00', 'WATER'),
       (37.5677, 126.9792, 5, 7, '2024-08-13 04:45:00', 'PEE'),
       (37.5678, 126.9793, 6, 7, '2024-08-13 04:55:00', 'POOP'),
       (37.5679, 126.9794, 7, 8, '2024-08-10 05:15:00', 'WATER'),
       (37.5680, 126.9795, 8, 8, '2024-08-10 05:25:00', 'PEE'),
       (37.5681, 126.9796, 8, 9, '2024-08-11 05:45:00', 'POOP'),
       (37.5682, 126.9797, 9, 9, '2024-08-11 05:55:00', 'WATER'),
       (37.5683, 126.9798, 7, 10, '2024-08-12 06:20:00', 'PEE'),
       (37.5684, 126.9799, 9, 10, '2024-08-12 06:30:00', 'POOP'),
       (37.5685, 126.9800, 10, 11, '2024-08-10 06:45:00', 'WATER'),
       (37.5686, 126.9801, 10, 12, '2024-08-11 07:15:00', 'PEE'),
       (37.5687, 126.9802, 10, 13, '2024-08-12 07:45:00', 'POOP'),
       (37.5688, 126.9803, 10, 14, '2024-08-13 08:25:00', 'WATER'),
       (37.5689, 126.9804, 10, 15, '2024-08-14 08:55:00', 'PEE'),
       (37.5690, 126.9805, 11, 16, '2024-08-10 09:15:00', 'POOP'),
       (37.5691, 126.9806, 11, 17, '2024-08-11 09:45:00', 'WATER'),
       (37.5692, 126.9807, 11, 18, '2024-08-12 10:20:00', 'PEE');

-- 카테고리 테이블
insert into post_category(category_id, category_name)
values (1, '공지사항');
insert into post_category(category_id, category_name)
values (2, '가입 인사');
insert into post_category(category_id, category_name)
values (3, '정보 공유');
insert into post_category(category_id, category_name)
values (4, '산책 인증');
insert into post_category(category_id, category_name)
values (5, '자유 게시판');
insert into post_category(category_id, category_name)
values (6, '리뷰 게시판');
insert into post_category(category_id, category_name)
values (7, 'QnA');

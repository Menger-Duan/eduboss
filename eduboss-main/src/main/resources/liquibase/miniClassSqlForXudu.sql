春季的规律
同一个人
同科目
同年份
年级规律
春 暑   秋
x  x+1  x+1

DESC `mini_class`

春季报表
SELECT a.blCampusName blCampusName ,a.mcName springMCName, a.springTeacherName
,a.springYearName, a.springQuarterName
,a.phaseName
,a.courseWeekday
,a.springGradeName
,a.springSubjectName
,a.classTypeName
,a.studentId
,a.studentName
,case WHEN b.summerTeacherId=a.springTeacherId THEN 1 ELSE 0 END originSpring1Teacher
,case WHEN IFNULL(b.mcName, 0)=0 THEN 0  ELSE 1 END XuBaoSummer
,b.mcName summerMCName
,b.classTypeName summerClassType
,b.summerTeacherName
,b.summerTeacherId
,case WHEN c.autumnTeacherId=a.springTeacherId THEN 1 ELSE 0 END originSpring2Teacher
,case WHEN IFNULL(c.mcName, 0)=0 THEN 0  ELSE 1 END XuBaoAutumn
,c.mcName autumnMCName
,c.classTypeName autumnClassType
,c.autumnTeacherName
,c.autumnTeacherId
 FROM
(
SELECT
mc.`NAME` mcName, mcs.MINI_CLASS_ID miniClassId, mcs.STUDENT_ID studentId
, springSubject.`NAME` springSubjectName,springYear.`NAME` springYearName,springQuarter.`NAME` springQuarterName
,springGrade.`NAME` springGradeName, springGrade.ID springGradeId
,blCampus.name blCampusName
,springTeacher.name springTeacherName, springTeacher.USER_ID springTeacherId
,phase.name phaseName
,classType.name classTypeName
,stu.name studentName
,mc.course_weekday courseWeekday
FROM mini_class_student mcs, mini_class mc
, data_dict springSubject, data_dict springGrade, data_dict springYear, product springProduct , data_dict springQuarter
, organization blCampus
, `user` springTeacher
, data_dict phase
, data_dict classType
,student stu
WHERE
mcs.MINI_CLASS_ID=mc.MINI_CLASS_ID
AND phase.id=springProduct.SMALL_CLASS_PHASE_ID
AND classType.id=springProduct.CLASS_TYPE_ID
AND stu.id=mcs.STUDENT_ID
AND mc.TEACHER_ID=springTeacher.USER_ID
AND mc.BL_CAMPUS_ID=blCampus.id
AND springSubject.ID=mc.`SUBJECT`
AND springGrade.ID=mc.GRADE
AND springYear.ID=springProduct.PRODUCT_VERSION_ID
AND mc.PRODUCE_ID=springProduct.ID
AND springQuarter.ID=springProduct.PRODUCT_QUARTER_ID
AND springQuarter.`NAME`="春季") a

LEFT JOIN
(
  SELECT mc.`NAME` mcName, mcs.STUDENT_ID studentId, summerSubject.`NAME` summerSubjectName
  ,summerYear.`NAME` summerYearName
, summerGrade.ID summerGradeId
, summerTeacher.USER_ID summerTeacherId
, summerTeacher.name summerTeacherName
, classType.name classTypeName
  FROM mini_class_student mcs, mini_class mc
 , data_dict summerSubject, data_dict summerGrade, data_dict summerYear, product summerProduct , data_dict summerQuarter
 ,`user` summerTeacher
 , data_dict classType
  WHERE
  mcs.MINI_CLASS_ID=mc.MINI_CLASS_ID
  AND classType.id=summerProduct.CLASS_TYPE_ID
  AND summerSubject.ID=mc.`SUBJECT`
  AND summerTeacher.USER_ID=mc.TEACHER_ID
  AND summerGrade.ID=mc.GRADE
  AND summerYear.ID=summerProduct.PRODUCT_VERSION_ID
  AND mc.PRODUCE_ID=summerProduct.ID
  AND summerQuarter.ID=summerProduct.PRODUCT_QUARTER_ID
  AND summerQuarter.`NAME`="暑假"
) b
ON a.studentId=b.studentId
AND a.springSubjectName=b.summerSubjectName
AND a.springYearName=b.summerYearName
AND nextGrade(a.springGradeId)=b.summerGradeId

LEFT JOIN
(
 SELECT mc.`NAME` mcName, mcs.STUDENT_ID studentId, autumnSubject.`NAME` autumnSubjectName
  ,autumnYear.`NAME` autumnYearName
, autumnGrade.ID autumnGradeId
, autumnTeacher.USER_ID autumnTeacherId
, autumnTeacher.name autumnTeacherName
, classType.name classTypeName
  FROM mini_class_student mcs, mini_class mc
 , data_dict autumnSubject, data_dict autumnGrade, data_dict autumnYear, product autumnProduct , data_dict autumnQuarter
 ,`user` autumnTeacher
, data_dict classType
  WHERE
  mcs.MINI_CLASS_ID=mc.MINI_CLASS_ID
  AND autumnSubject.ID=mc.`SUBJECT`
  AND classType.id=autumnProduct.CLASS_TYPE_ID
  AND autumnTeacher.USER_ID=mc.TEACHER_ID
  AND autumnGrade.ID=mc.GRADE
  AND autumnYear.ID=autumnProduct.PRODUCT_VERSION_ID
  AND mc.PRODUCE_ID=autumnProduct.ID
  AND autumnQuarter.ID=autumnProduct.PRODUCT_QUARTER_ID
  AND autumnQuarter.`NAME`="秋季"
) c
ON a.studentId=c.studentId
AND a.springSubjectName=c.autumnSubjectName
AND a.springYearName=c.autumnYearName
AND nextGrade(a.springGradeId)=c.autumnGradeId













暑假
SELECT b.blCampusName blCampusName ,b.mcName summerMCName, b.summerTeacherName
,b.summerYearName, b.summerQuarterName
,b.phaseName
,b.courseWeekday
,b.summerGradeName
,b.summerSubjectName
,b.classTypeName
,b.studentId
,b.studentName
,case WHEN c.autumnTeacherId=b.summerTeacherId THEN 1 ELSE 0 END originSummerTeacher
,case WHEN IFNULL(c.mcName, 0)=0 THEN 0  ELSE 1 END XuBaoAutumn
,c.mcName autumnMCName
,c.classTypeName autumnClassType
,c.autumnTeacherName
,c.autumnTeacherId
FROM
(
SELECT
mc.`NAME` mcName, mcs.MINI_CLASS_ID miniClassId, mcs.STUDENT_ID studentId
, summerSubject.`NAME` summerSubjectName,summerYear.`NAME` summerYearName,summerQuarter.`NAME` summerQuarterName
,summerGrade.`NAME` summerGradeName, summerGrade.ID summerGradeId
,blCampus.name blCampusName
,summerTeacher.name summerTeacherName, summerTeacher.USER_ID summerTeacherId
,phase.name phaseName
,classType.name classTypeName
,stu.name studentName
,mc.course_weekday courseWeekday
FROM mini_class_student mcs, mini_class mc
, data_dict summerSubject, data_dict summerGrade, data_dict summerYear, product summerProduct , data_dict summerQuarter
, organization blCampus
, `user` summerTeacher
, data_dict phase
, data_dict classType
,student stu
WHERE
mcs.MINI_CLASS_ID=mc.MINI_CLASS_ID
AND phase.id=summerProduct.SMALL_CLASS_PHASE_ID
AND classType.id=summerProduct.CLASS_TYPE_ID
AND stu.id=mcs.STUDENT_ID
AND mc.TEACHER_ID=summerTeacher.USER_ID
AND mc.BL_CAMPUS_ID=blCampus.id
AND summerSubject.ID=mc.`SUBJECT`
AND summerGrade.ID=mc.GRADE
AND summerYear.ID=summerProduct.PRODUCT_VERSION_ID
AND mc.PRODUCE_ID=summerProduct.ID
AND summerQuarter.ID=summerProduct.PRODUCT_QUARTER_ID
AND summerQuarter.`NAME`="暑假"
) b
LEFT JOIN
(
SELECT mc.`NAME` mcName, mcs.STUDENT_ID studentId, autumnSubject.`NAME` autumnSubjectName
  ,autumnYear.`NAME` autumnYearName
, autumnGrade.ID autumnGradeId
, autumnTeacher.USER_ID autumnTeacherId
, autumnTeacher.name autumnTeacherName
, classType.name classTypeName
  FROM mini_class_student mcs, mini_class mc
 , data_dict autumnSubject, data_dict autumnGrade, data_dict autumnYear, product autumnProduct , data_dict autumnQuarter
 ,`user` autumnTeacher
 , data_dict classType
  WHERE
  mcs.MINI_CLASS_ID=mc.MINI_CLASS_ID
  AND classType.id=autumnProduct.CLASS_TYPE_ID
  AND autumnSubject.ID=mc.`SUBJECT`
  AND autumnTeacher.USER_ID=mc.TEACHER_ID
  AND autumnGrade.ID=mc.GRADE
  AND autumnYear.ID=autumnProduct.PRODUCT_VERSION_ID
  AND mc.PRODUCE_ID=autumnProduct.ID
  AND autumnQuarter.ID=autumnProduct.PRODUCT_QUARTER_ID
  AND autumnQuarter.`NAME`="秋季"
) c
ON
b.studentId=c.studentId
AND b.summerSubjectName=c.autumnSubjectName
AND b.summerYearName=c.autumnYearName
AND b.summerGradeId=c.autumnGradeId
























秋季
SELECT c.blCampusName blCampusName ,c.mcName autumnMCName, c.autumnTeacherName
,c.autumnYearName, c.autumnQuarterName
,c.phaseName
,c.courseWeekday
,c.autumnGradeName
,c.autumnSubjectName
,c.classTypeName
,c.studentId
,c.studentName
,case WHEN d.winterTeacherId=c.autumnTeacherId THEN 1 ELSE 0 END originAutumn1Teacher
,case WHEN IFNULL(d.mcName, 0)=0 THEN 0  ELSE 1 END XuBaoWinter
,d.mcName winterMCName
,d.classTypeName winterClassType
,d.winterTeacherName
,d.winterTeacherId
,case WHEN a.springTeacherId=c.autumnTeacherId THEN 1 ELSE 0 END originAutumn2Teacher
,case WHEN IFNULL(a.mcName, 0)=0 THEN 0  ELSE 1 END XuBaoSpring
,a.mcName springMCName
,a.classTypeName springClassType
,a.springTeacherName
,a.springTeacherId
 FROM
(
SELECT
mc.`NAME` mcName, mcs.MINI_CLASS_ID miniClassId, mcs.STUDENT_ID studentId
, autumnSubject.`NAME` autumnSubjectName,autumnYear.`NAME` autumnYearName,autumnQuarter.`NAME` autumnQuarterName
,autumnGrade.`NAME` autumnGradeName, autumnGrade.ID autumnGradeId
,blCampus.name blCampusName
,autumnTeacher.name autumnTeacherName, autumnTeacher.USER_ID autumnTeacherId
,phase.name phaseName
,classType.name classTypeName
,stu.name studentName
,mc.course_weekday courseWeekday
FROM mini_class_student mcs, mini_class mc
, data_dict autumnSubject, data_dict autumnGrade, data_dict autumnYear, product autumnProduct , data_dict autumnQuarter
, organization blCampus
, `user` autumnTeacher
, data_dict phase
, data_dict classType
,student stu
WHERE
mcs.MINI_CLASS_ID=mc.MINI_CLASS_ID
AND phase.id=autumnProduct.SMALL_CLASS_PHASE_ID
AND classType.id=autumnProduct.CLASS_TYPE_ID
AND stu.id=mcs.STUDENT_ID
AND mc.TEACHER_ID=autumnTeacher.USER_ID
AND mc.BL_CAMPUS_ID=blCampus.id
AND autumnSubject.ID=mc.`SUBJECT`
AND autumnGrade.ID=mc.GRADE
AND autumnYear.ID=autumnProduct.PRODUCT_VERSION_ID
AND mc.PRODUCE_ID=autumnProduct.ID
AND autumnQuarter.ID=autumnProduct.PRODUCT_QUARTER_ID
AND autumnQuarter.`NAME`="秋季") c

LEFT JOIN
(
  SELECT mc.`NAME` mcName, mcs.STUDENT_ID studentId, winterSubject.`NAME` winterSubjectName
  ,winterYear.`NAME` winterYearName
, winterGrade.ID winterGradeId
, winterTeacher.USER_ID winterTeacherId
, winterTeacher.name winterTeacherName
, classType.name classTypeName
  FROM mini_class_student mcs, mini_class mc
 , data_dict winterSubject, data_dict winterGrade, data_dict winterYear, product winterProduct , data_dict winterQuarter
 ,`user` winterTeacher
 , data_dict classType
  WHERE
  mcs.MINI_CLASS_ID=mc.MINI_CLASS_ID
  AND classType.id=winterProduct.CLASS_TYPE_ID
  AND winterSubject.ID=mc.`SUBJECT`
  AND winterTeacher.USER_ID=mc.TEACHER_ID
  AND winterGrade.ID=mc.GRADE
  AND winterYear.ID=winterProduct.PRODUCT_VERSION_ID
  AND mc.PRODUCE_ID=winterProduct.ID
  AND winterQuarter.ID=winterProduct.PRODUCT_QUARTER_ID
  AND winterQuarter.`NAME`="寒假"
) d
ON  c.studentId=d.studentId
AND c.autumnSubjectName=d.winterSubjectName
AND nextYear(c.autumnYearName)=d.winterYearName
AND c.autumnGradeId=d.winterGradeId

LEFT JOIN
(
 SELECT mc.`NAME` mcName, mcs.STUDENT_ID studentId, springSubject.`NAME` springSubjectName
  ,springYear.`NAME` springYearName
, springGrade.ID springGradeId
, springTeacher.USER_ID springTeacherId
, springTeacher.name springTeacherName
, classType.name classTypeName
  FROM mini_class_student mcs, mini_class mc
 , data_dict springSubject, data_dict springGrade, data_dict springYear, product springProduct , data_dict springQuarter
 ,`user` springTeacher
, data_dict classType
  WHERE
  mcs.MINI_CLASS_ID=mc.MINI_CLASS_ID
  AND springSubject.ID=mc.`SUBJECT`
  AND classType.id=springProduct.CLASS_TYPE_ID
  AND springTeacher.USER_ID=mc.TEACHER_ID
  AND springGrade.ID=mc.GRADE
  AND springYear.ID=springProduct.PRODUCT_VERSION_ID
  AND mc.PRODUCE_ID=springProduct.ID
  AND springQuarter.ID=springProduct.PRODUCT_QUARTER_ID
  AND springQuarter.`NAME`="春季"
) a
ON c.studentId=a.studentId
AND c.autumnSubjectName=a.springSubjectName
AND nextYear(c.autumnYearName)=a.springYearName
AND c.autumnGradeId=a.springGradeId





















SELECT d.blCampusName blCampusName ,d.mcName winterMCName, d.winterTeacherName
,d.winterYearName, d.winterQuarterName
,d.phaseName
,d.courseWeekday
,d.winterGradeName
,d.winterSubjectName
,d.classTypeName
,d.studentId
,d.studentName
,case WHEN a.springTeacherId=d.winterTeacherId THEN 1 ELSE 0 END originSpringTeacher
,case WHEN IFNULL(a.mcName, 0)=0 THEN 0  ELSE 1 END XuBaoSpring
,a.mcName springMCName
,a.classTypeName springClassType
,a.springTeacherName
,a.springTeacherId
FROM
(
SELECT
mc.`NAME` mcName, mcs.MINI_CLASS_ID miniClassId, mcs.STUDENT_ID studentId
, winterSubject.`NAME` winterSubjectName,winterYear.`NAME` winterYearName,winterQuarter.`NAME` winterQuarterName
,winterGrade.`NAME` winterGradeName, winterGrade.ID winterGradeId
,blCampus.name blCampusName
,winterTeacher.name winterTeacherName, winterTeacher.USER_ID winterTeacherId
,phase.name phaseName
,classType.name classTypeName
,stu.name studentName
,mc.course_weekday courseWeekday
FROM mini_class_student mcs, mini_class mc
, data_dict winterSubject, data_dict winterGrade, data_dict winterYear, product winterProduct , data_dict winterQuarter
, organization blCampus
, `user` winterTeacher
, data_dict phase
, data_dict classType
,student stu
WHERE
mcs.MINI_CLASS_ID=mc.MINI_CLASS_ID
AND phase.id=winterProduct.SMALL_CLASS_PHASE_ID
AND classType.id=winterProduct.CLASS_TYPE_ID
AND stu.id=mcs.STUDENT_ID
AND mc.TEACHER_ID=winterTeacher.USER_ID
AND mc.BL_CAMPUS_ID=blCampus.id
AND winterSubject.ID=mc.`SUBJECT`
AND winterGrade.ID=mc.GRADE
AND winterYear.ID=winterProduct.PRODUCT_VERSION_ID
AND mc.PRODUCE_ID=winterProduct.ID
AND winterQuarter.ID=winterProduct.PRODUCT_QUARTER_ID
AND winterQuarter.`NAME`="寒假"
) d
LEFT JOIN
(
SELECT mc.`NAME` mcName, mcs.STUDENT_ID studentId, springSubject.`NAME` springSubjectName
  ,springYear.`NAME` springYearName
, springGrade.ID springGradeId
, springTeacher.USER_ID springTeacherId
, springTeacher.name springTeacherName
, classType.name classTypeName
  FROM mini_class_student mcs, mini_class mc
 , data_dict springSubject, data_dict springGrade, data_dict springYear, product springProduct , data_dict springQuarter
 ,`user` springTeacher
 , data_dict classType
  WHERE
  mcs.MINI_CLASS_ID=mc.MINI_CLASS_ID
  AND classType.id=springProduct.CLASS_TYPE_ID
  AND springSubject.ID=mc.`SUBJECT`
  AND springTeacher.USER_ID=mc.TEACHER_ID
  AND springGrade.ID=mc.GRADE
  AND springYear.ID=springProduct.PRODUCT_VERSION_ID
  AND mc.PRODUCE_ID=springProduct.ID
  AND springQuarter.ID=springProduct.PRODUCT_QUARTER_ID
  AND springQuarter.`NAME`="春季"
) a
ON
d.studentId=a.studentId
AND d.winterSubjectName=a.springSubjectName
AND d.winterYearName=a.springYearName
AND d.winterGradeId=a.springGradeId
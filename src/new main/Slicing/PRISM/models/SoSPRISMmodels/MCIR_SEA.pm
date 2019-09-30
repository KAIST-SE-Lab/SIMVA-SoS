dtmc

//const double rate_SoS;		// probability for following SoS purpose_PTS1 (ACKNOWLODGED SoS)
//const double rate_info; // probability for infomation update (COLLABORATIVE SoS)
//const double rate_new_PTS1;
//const double rate_new_non_PTS1 = 0;

const double PR_HELI1;
const double PR_HELI2;
const double PR_HELI3;
const double PR_HELI4;
const double PR_HELI5;
const double PR_HELI6;
const double PR_HELI7;
const double PR_HELI8;
const double PR_HELI9;
const double PR_HELI10;

//const int MCI_MAX = 200;

const int SEA_MAX;

//const int ETC_MAX = 50;
//const int MCI_PATIENT_OCCUR = 50;

const int SEA_PATIENT_OCCUR = 10;

//const int ETC_PATIENT_OCCUR = 2;

const int Hospital_MAX = 50;
const int doctor_MAX = 35;

const int locA = 0;
const int locB = 1;
const int locC = 2;
const int locD = 3;
const int locE = 4;
const int locF = 5;
const int locG = 6;
const int locH = 7;

const int to_MCI = 0;
const int to_ETC = 1;
const int not_determined = 2;

//const double PR_MCI_OCCUR =1/2;
const double PR_SEA_OCCUR = 1/5;
//const double PR_ETC_OCCUR =1/2;
//const double PR_MCI_DEAD = 1/10;
const double PR_SEA_DEAD = 1/20;
//const double PR_ETC_DEAD = 1/50;
//const double PR_WAIT_DEAD = 1/100;
//const double PR_OPERATE_DEAD = 5/100;
//const double PR_PTS1_DEAD = 1/2;
//const double PR_PTS2_DEAD = 2/100;
//const double PR_PTS3_DEAD = 3/100;
//const double PR_PTS4_DEAD = 4/100;
//const double PR_PTS5_DEAD = 5/100;
//const double PR_PTS6_DEAD = 6/100;
//const double PR_PTS7_DEAD = 7/100;
//const double PR_PTS8_DEAD = 8/100;
//const double PR_PTS9_DEAD = 9/100;
//const double PR_PTS10_DEAD = 10/100;

formula SEA_formula = (rescued_SEA + dead_SEA + curr_SEA + temp_occur_SEA + temp_dead_SEA >= 100);

module env_SEA
	curr_SEA:[0..SEA_MAX] init 0;
	rescued_SEA:[0..SEA_MAX] init 0;
	dead_SEA:[0..SEA_MAX] init 0;
	temp_occur_SEA:[0..SEA_MAX] init 0;
	temp_dead_SEA:[0..SEA_MAX] init 0;
	occur_SEA:bool init false;
	death_SEA:bool init false;
	
	[time] occur_SEA & death_SEA -> (curr_SEA'=curr_SEA+temp_occur_SEA-temp_dead_SEA) & (dead_SEA'=dead_SEA+temp_dead_SEA) & (temp_occur_SEA'=0) & (temp_dead_SEA'=0) & (occur_SEA'=false) & (death_SEA'=false);
	[time] !occur_SEA & death_SEA -> (curr_SEA'=curr_SEA+temp_occur_SEA-temp_dead_SEA) & (dead_SEA'=dead_SEA+temp_dead_SEA) & (temp_occur_SEA'=0) & (temp_dead_SEA'=0) & (death_SEA'=false);
	[time] occur_SEA & !death_SEA -> (curr_SEA'=curr_SEA+temp_occur_SEA-temp_dead_SEA) & (dead_SEA'=dead_SEA+temp_dead_SEA) & (temp_occur_SEA'=0) & (temp_dead_SEA'=0) & (occur_SEA'=false);
	[time] !occur_SEA &  !death_SEA & SEA_formula -> true;
				
	[patient_occur_SEA] !SEA_formula -> PR_SEA_OCCUR:(temp_occur_SEA'=temp_occur_SEA+SEA_PATIENT_OCCUR) & (occur_SEA'=true) + (1-PR_SEA_OCCUR): (occur_SEA'=true);
	[patient_dead_SEA] curr_SEA>0 -> PR_SEA_DEAD:(temp_dead_SEA'=min(temp_dead_SEA+1,SEA_MAX))  & (death_SEA'=true) + (1-PR_SEA_DEAD):(death_SEA'=true);
	
	[MCI_rescued_by_HELICOPTER1] curr_SEA>0 -> (rescued_SEA'=min(rescued_SEA+1,SEA_MAX)) & (curr_SEA'=curr_SEA-1); 
	[MCI_no_rescued_by_HELICOPTER1] curr_SEA=0 -> true;
	[MCI_rescued_by_HELICOPTER2] curr_SEA>0 -> (rescued_SEA'=min(rescued_SEA+1,SEA_MAX)) & (curr_SEA'=curr_SEA-1); 
	[MCI_no_rescued_by_HELICOPTER2] curr_SEA=0 -> true;
	[MCI_rescued_by_HELICOPTER3] curr_SEA>0 -> (rescued_SEA'=min(rescued_SEA+1,SEA_MAX)) & (curr_SEA'=curr_SEA-1); 
	[MCI_no_rescued_by_HELICOPTER3] curr_SEA=0 -> true;
	[MCI_rescued_by_HELICOPTER4] curr_SEA>0 -> (rescued_SEA'=min(rescued_SEA+1,SEA_MAX)) & (curr_SEA'=curr_SEA-1); 
	[MCI_no_rescued_by_HELICOPTER4] curr_SEA=0 -> true;
	[MCI_rescued_by_HELICOPTER5] curr_SEA>0 -> (rescued_SEA'=min(rescued_SEA+1,SEA_MAX)) & (curr_SEA'=curr_SEA-1); 
	[MCI_no_rescued_by_HELICOPTER5] curr_SEA=0 -> true;
endmodule

module Hospital1
	isParticipated_H1: bool init false;
	state_H1:[0..1] init 0;
//	num_of_patient_MCI:[0..Hospital_MAX] init 0;
	num_of_patient_SEA_H1:[0..Hospital_MAX] init 0;
//	num_of_dead_patient_MCI_hospital:[0..MCI_MAX] init 0;
//	num_of_cured_patient_MCI:[0..MCI_MAX] init 0;
	num_of_cured_patient_SEA_H1:[0..SEA_MAX] init 0;
	
	[]isParticipated_H1 -> (state_H1'=1);
	[heli1_arrive_hospital1_with_patient] state_H1=1 -> (num_of_patient_SEA_H1'=num_of_patient_SEA_H1+1);
	[heli1_arrive_hospital1_with_no_patient] state_H1=1 -> true;
	[heli2_arrive_hospita1l_with_patient] state_H1=1 -> (num_of_patient_SEA_H1'=num_of_patient_SEA_H1+1);
	[heli2_arrive_hospital1_with_no_patient] state_H1=1 -> true;
	[heli3_arrive_hospital1_with_patient] state_H1=1 -> (num_of_patient_SEA_H1'=num_of_patient_SEA_H1+1);
	[heli3_arrive_hospital1_with_no_patient] state_H1=1 -> true;
	[heli4_arrive_hospital1_with_patient] state_H1=1 -> (num_of_patient_SEA_H1'=num_of_patient_SEA_H1+1);
	[heli4_arrive_hospital1_with_no_patient] state_H1=1 -> true;
	[heli5_arrive_hospital1_with_patient] state_H1=1 -> (num_of_patient_SEA_H1'=num_of_patient_SEA_H1+1);
	[heli5_arrive_hospital1_with_no_patient] state_H1=1 -> true;
	[heli6_arrive_hospital1_with_patient] state_H1=1 -> (num_of_patient_SEA_H1'=num_of_patient_SEA_H1+1);
	[heli6_arrive_hospital1_with_no_patient] state_H1=1 -> true;
	[heli7_arrive_hospita1l_with_patient] state_H1=1 -> (num_of_patient_SEA_H1'=num_of_patient_SEA_H1+1);
	[heli7_arrive_hospital1_with_no_patient] state_H1=1 -> true;
	[heli8_arrive_hospital1_with_patient] state_H1=1 -> (num_of_patient_SEA_H1'=num_of_patient_SEA_H1+1);
	[heli8_arrive_hospital1_with_no_patient] state_H1=1 -> true;
	[heli9_arrive_hospital1_with_patient] state_H1=1 -> (num_of_patient_SEA_H1'=num_of_patient_SEA_H1+1);
	[heli9_arrive_hospital1_with_no_patient] state_H1=1 -> true;
	[heli10_arrive_hospital1_with_patient] state_H1=1 -> (num_of_patient_SEA_H1'=num_of_patient_SEA_H1+1);
	[heli10_arrive_hospital1_with_no_patient] state_H1=1 -> true;
	
	[cure_H1]num_of_patient_SEA_H1>0 -> (num_of_patient_SEA_H1'=num_of_patient_SEA_H1-1) & (num_of_cured_patient_SEA_H1'=num_of_cured_patient_SEA_H1+1);				
endmodule

module Hospital2 = Hospital1[
	isParticipated_H1 =isParticipated_H2,
	state_H1 = state_H2,
	num_of_patient_SEA_H1 = num_of_patient_SEA_H2,
	num_of_cured_patient_SEA_H1 = num_of_cured_patient_SEA_H2,
	heli1_arrive_hospital1_with_patient = heli1_arrive_hospital2_with_patient,
	heli1_arrive_hospital1_with_no_patient = heli1_arrive_hospital2_with_no_patient,
	heli2_arrive_hospita1l_with_patient = heli2_arrive_hospita12_with_patient,
	heli2_arrive_hospital1_with_no_patient = heli2_arrive_hospital2_with_no_patient,
	heli3_arrive_hospital1_with_patient = heli3_arrive_hospital3_with_patient,
	heli3_arrive_hospital1_with_no_patient = heli3_arrive_hospital2_with_no_patient,
	heli4_arrive_hospital1_with_patient = heli4_arrive_hospital4_with_patient,
	heli4_arrive_hospital1_with_no_patient = heli4_arrive_hospital4_with_no_patient,
	heli5_arrive_hospital1_with_patient = heli5_arrive_hospital5_with_patient,
	heli5_arrive_hospital1_with_no_patient = heli5_arrive_hospital5_with_no_patient,
	heli6_arrive_hospital1_with_patient = heli1_arrive_hospital2_with_patient,
	heli6_arrive_hospital1_with_no_patient = heli1_arrive_hospital2_with_no_patient,
	heli7_arrive_hospita1l_with_patient = heli2_arrive_hospita12_with_patient,
	heli7_arrive_hospital1_with_no_patient = heli2_arrive_hospital2_with_no_patient,
	heli8_arrive_hospital1_with_patient = heli3_arrive_hospital3_with_patient,
	heli8_arrive_hospital1_with_no_patient = heli3_arrive_hospital2_with_no_patient,
	heli9_arrive_hospital1_with_patient = heli4_arrive_hospital4_with_patient,
	heli9_arrive_hospital1_with_no_patient = heli4_arrive_hospital4_with_no_patient,
	heli10_arrive_hospital1_with_patient = heli5_arrive_hospital5_with_patient,
	heli10_arrive_hospital1_with_no_patient = heli5_arrive_hospital5_with_no_patient,
	cure_H1 = cure_H2
]endmodule

module HELICOPTER1
	isParticipated1: bool init false;
	patient1_SEA:[0..3] init 3; //0:none, 1:alive, 2:dead
	heli1_location:[0..2] init 2;
	[] isParticipated1 -> (heli1_location'=0) & (patient1_SEA'=0);
	[time] heli1_location=0 & patient1_SEA=0 -> (heli1_location'=1);
	[MCI_rescued_by_HELICOPTER1] heli1_location=1 -> PR_HELI1:(patient1_SEA'=1) + (1-PR_HELI1):(patient1_SEA'=2);
	[MCI_no_rescued_by_HELICOPTER1] true -> (heli1_location'=0);
//	[] patient1_SEA=1 -> (patient1_SEA'=2);
//	[] patient1_SEA=2 -> PR_HELI1:(patient1_SEA'=1)+(1-PR_HELI1):true;
	[heli1_arrive_hospital1_with_patient] patient1_SEA=1 -> (patient1_SEA'=0);
	[heli1_arrive_hospital1_with_no_patient] patient1_SEA=0|patient1_SEA=2 -> (patient1_SEA'=0);
	[heli1_arrive_hospital2_with_patient] patient1_SEA=1 -> (patient1_SEA'=0);
	[heli1_arrive_hospital2_with_no_patient] patient1_SEA=0|patient1_SEA=2 -> (patient1_SEA'=0);	
endmodule

module HELICOPTER2 = HELICOPTER1[
	isParticipated1 = isParticipated2,
	patient1_SEA = patient2_SEA,
	PR_HELI1 = PR_HELI2,
	heli1_location = heli2_location,
	MCI_rescued_by_HELICOPTER1 = MCI_rescued_by_HELICOPTER2,
	MCI_no_rescued_by_HELICOPTER1 = MCI_no_rescued_by_HELICOPTER2,
	heli1_arrive_hospital1_with_patient = heli2_arrive_hospita1l_with_patient,
	heli1_arrive_hospital1_with_no_patient = heli2_arrive_hospital1_with_no_patient,
	heli1_arrive_hospital2_with_patient = heli2_arrive_hospital2_with_patient,
	heli1_arrive_hospital2_with_no_patient = heli2_arrive_hospital2_with_no_patient
]endmodule

module HELICOPTER3 = HELICOPTER1[
	isParticipated1 = isParticipated3,
	patient1_SEA = patient3_SEA,
	PR_HELI1 = PR_HELI3,
	heli1_location = heli3_location,
	MCI_rescued_by_HELICOPTER1 = MCI_rescued_by_HELICOPTER3,
	MCI_no_rescued_by_HELICOPTER1 = MCI_no_rescued_by_HELICOPTER3,
	heli1_arrive_hospital1_with_patient = heli3_arrive_hospital1_with_patient,
	heli1_arrive_hospital1_with_no_patient = heli3_arrive_hospital1_with_no_patient,
	heli1_arrive_hospital2_with_patient = heli3_arrive_hospital2_with_patient,
	heli1_arrive_hospital2_with_no_patient = heli3_arrive_hospital2_with_no_patient
]endmodule

module HELICOPTER4 = HELICOPTER1[
	isParticipated1 = isParticipated4,
	patient1_SEA = patient4_SEA,
	PR_HELI1 = PR_HELI4,
	heli1_location = heli4_location,
	MCI_rescued_by_HELICOPTER1 = MCI_rescued_by_HELICOPTER4,
	MCI_no_rescued_by_HELICOPTER1 = MCI_no_rescued_by_HELICOPTER4,
	heli1_arrive_hospital1_with_patient = heli4_arrive_hospital1_with_patient,
	heli1_arrive_hospital1_with_no_patient = heli4_arrive_hospital1_with_no_patient,
	heli1_arrive_hospital2_with_patient = heli4_arrive_hospital2_with_patient,
	heli1_arrive_hospital2_with_no_patient = heli4_arrive_hospital2_with_no_patient
]endmodule

module HELICOPTER5 = HELICOPTER1[
	isParticipated1 = isParticipated5,
	patient1_SEA = patient5_SEA,
	PR_HELI1 = PR_HELI5,
	heli1_location = heli5_location,
	MCI_rescued_by_HELICOPTER1 = MCI_rescued_by_HELICOPTER5,
	MCI_no_rescued_by_HELICOPTER1 = MCI_no_rescued_by_HELICOPTER5,
	heli1_arrive_hospital1_with_patient = heli5_arrive_hospital1_with_patient,
	heli1_arrive_hospital1_with_no_patient = heli5_arrive_hospital1_with_no_patient,
	heli1_arrive_hospital2_with_patient = heli5_arrive_hospital2_with_patient,
	heli1_arrive_hospital2_with_no_patient = heli5_arrive_hospital2_with_no_patient
]endmodule

module HELICOPTER6 = HELICOPTER1[
	isParticipated1 = isParticipated6,
	patient1_SEA = patient6_SEA,
	PR_HELI1 = PR_HELI6,
	heli1_location = heli6_location,
	MCI_rescued_by_HELICOPTER1 = MCI_rescued_by_HELICOPTER6,
	MCI_no_rescued_by_HELICOPTER1 = MCI_no_rescued_by_HELICOPTER6,
	heli1_arrive_hospital1_with_patient = heli6_arrive_hospita1l_with_patient,
	heli1_arrive_hospital1_with_no_patient = heli6_arrive_hospital1_with_no_patient,
	heli1_arrive_hospital6_with_patient = heli6_arrive_hospital6_with_patient,
	heli1_arrive_hospital6_with_no_patient = heli6_arrive_hospital6_with_no_patient
]endmodule

module HELICOPTER7 = HELICOPTER1[
	isParticipated1 = isParticipated7,
	patient1_SEA = patient7_SEA,
	PR_HELI1 = PR_HELI7,
	heli1_location = heli7_location,
	MCI_rescued_by_HELICOPTER1 = MCI_rescued_by_HELICOPTER7,
	MCI_no_rescued_by_HELICOPTER1 = MCI_no_rescued_by_HELICOPTER7,
	heli1_arrive_hospital1_with_patient = heli7_arrive_hospital1_with_patient,
	heli1_arrive_hospital1_with_no_patient = heli7_arrive_hospital1_with_no_patient,
	heli1_arrive_hospital2_with_patient = heli7_arrive_hospital2_with_patient,
	heli1_arrive_hospital2_with_no_patient = heli7_arrive_hospital2_with_no_patient
]endmodule

module HELICOPTER8 = HELICOPTER1[
	isParticipated1 = isParticipated8,
	patient1_SEA = patient8_SEA,
	PR_HELI1 = PR_HELI8,
	heli1_location = heli8_location,
	MCI_rescued_by_HELICOPTER1 = MCI_rescued_by_HELICOPTER8,
	MCI_no_rescued_by_HELICOPTER1 = MCI_no_rescued_by_HELICOPTER8,
	heli1_arrive_hospital1_with_patient = heli8_arrive_hospital1_with_patient,
	heli1_arrive_hospital1_with_no_patient = heli8_arrive_hospital1_with_no_patient,
	heli1_arrive_hospital2_with_patient = heli8_arrive_hospital2_with_patient,
	heli1_arrive_hospital2_with_no_patient = heli8_arrive_hospital2_with_no_patient
]endmodule

module HELICOPTER9 = HELICOPTER1[
	isParticipated1 = isParticipated9,
	patient1_SEA = patient9_SEA,
	PR_HELI1 = PR_HELI9,
	heli1_location = heli9_location,
	MCI_rescued_by_HELICOPTER1 = MCI_rescued_by_HELICOPTER9,
	MCI_no_rescued_by_HELICOPTER1 = MCI_no_rescued_by_HELICOPTER9,
	heli1_arrive_hospital1_with_patient = heli9_arrive_hospital1_with_patient,
	heli1_arrive_hospital1_with_no_patient = heli9_arrive_hospital1_with_no_patient,
	heli1_arrive_hospital2_with_patient = heli9_arrive_hospital2_with_patient,
	heli1_arrive_hospital2_with_no_patient = heli9_arrive_hospital2_with_no_patient
]endmodule

module HELICOPTER10 = HELICOPTER1[
	isParticipated1 = isParticipated10,
	patient1_SEA = patient10_SEA,
	PR_HELI1 = PR_HELI10,
	heli1_location = heli10_location,
	MCI_rescued_by_HELICOPTER1 = MCI_rescued_by_HELICOPTER10,
	MCI_no_rescued_by_HELICOPTER1 = MCI_no_rescued_by_HELICOPTER10,
	heli1_arrive_hospital1_with_patient = heli10_arrive_hospital1_with_patient,
	heli1_arrive_hospital1_with_no_patient = heli10_arrive_hospital1_with_no_patient,
	heli1_arrive_hospital2_with_patient = heli10_arrive_hospital2_with_patient,
	heli1_arrive_hospital2_with_no_patient = heli10_arrive_hospital2_with_no_patient
]endmodule
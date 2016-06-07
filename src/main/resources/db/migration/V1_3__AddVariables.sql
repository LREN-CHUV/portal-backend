--
-- Add variable
--

COPY variable (code, description, iscovariable, isfilter, isgrouping, isvariable, label, length, maxvalue, minvalue, type, units, group_code) FROM stdin;
RID	\N	f	f	f	t	RID	38	\N	\N	N	\N	no-group
PTID	\N	f	f	f	t	PTID	\N	\N	\N	T	\N	no-group
VISCODE	\N	t	f	t	t	VISCODE	20	\N	\N	T	\N	no-group
SITE	\N	t	f	t	t	SITE	\N	\N	\N	I	\N	source
COLPROT	\N	t	f	t	t	COLPROT	\N	\N	\N	T	\N	protocol
ORIGPROT	\N	t	f	t	t	ORIGPROT	\N	\N	\N	T	\N	protocol
EXAMDATE	\N	f	f	f	t	EXAMDATE	10	\N	\N	D	\N	date
DX_bl	Baseline diagnostic	t	f	t	t	DX_bl	\N	\N	\N	T	\N	diagnostic
AGE	Chronological age refers to the calendar age. It is the number of years that have elapsed from birth to the exam date.\n                                            Age remains the greatest risk factor for Alzheimer's and is thus a fundamental driver for development of the disease. Although Alzheimer's is not a normal part of growing older, the greatest risk factor for the disease is increasing age. Data from the multiple studies show that the incidence of Alzheimer's disease rises exponentially after the sixth decade of life. After age 65, the risk of Alzheimer's doubles every five years. After age 85, the risk reaches nearly 50 percent.\n                                            See: https://www.alzheimers.org.uk/site/scripts/documents_info.php?documentID=102\n                                            Brandalyn C.Riedel, Paul M.Thompson, Roberta Diaz Brinton, Age, APOE and Sex: Triad of Risk of Alzheimer's Disease, Journal of Steroid Biochemistry and Molecular Biology http://dx.doi.org/10.1016/j.jsbmb.2016.03.012	t	t	t	t	AGE	3	130	0	I	\N	age
PTGENDER	Refers to the socially constructed characteristics of women and men.\n                                            The female prevalence of AD is well documented and is generally attributed to the greater life span of women relative to men. Globally, women outlive men by an average of 4.5 years. However, survival of males is projected to be comparable to females with near equality in longevity between females and males.\n                                            See: Michelle M. Mielke, Prashanthi. Vemuri, Walter A. Rocca, Clinical epidemiology of Alzheimer's disease: assessing sex and gender differences. Clinical Epidemiology 2014:6 37–4.\n                                            Brandalyn C.Riedel, Paul M.Thompson, Roberta Diaz Brinton, Age, APOE and Sex: Triad of Risk of Alzheimer's Disease, Journal of Steroid Biochemistry and Molecular Biology http://dx.doi.org/10.1016/j.jsbmb.2016.03.012	t	f	t	t	PTGENDER	1	\N	\N	T	\N	genre
PTEDUCAT	Education level (years)	t	t	t	t	PTEDUCAT	2	20	0	I	\N	education
PTETHCAT	\N	t	f	t	t	PTETHCAT	\N	\N	\N	T	\N	ethny
PTRACCAT	\N	t	f	t	t	PTRACCAT	\N	\N	\N	T	\N	country
PTMARRY	\N	t	f	t	t	PTMARRY	\N	\N	\N	T	\N	marital
APOE4	Apolipoprotein E (APOE) gene is the strongest risk factor for Late Onset Alzheimer Disease (LOAD).\n                                            Risk genes increase the likelihood of developing a disease, but do not guarantee it will happen. Researchers have found several genes that increase the risk of Alzheimer's. APOE-e4 is the first risk gene identified, and remains the gene with strongest impact on risk. APOE-e4 is one of three common forms of the APOE gene; the others are APOE-e2 and APOE-e3. Everyone inherits a copy of some form of APOE from each parent. Those who inherit one copy of APOE-e4 have an increased risk of developing Alzheimer's. Those who inherit two copies have an even higher risk, but not a certainty. In addition to raising risk, APOE-e4 may tend to make symptoms appear at a younger age than usual. Scientists estimate that APOE-e4 is implicated in about 20 percent to 25 percent of Alzheimer's cases.\n                                            See Genetics and Alzheimer's:\n                                            http://www.alz.org/research/science/alzheimers_disease_causes.asp#apoe\n                                            More information:\n                                            Apolipoprotein E and Alzheimer disease: risk, mechanisms, and therapy.\n                                            Liu, C.-C. et al. Nat Rev Neurol . 2013 February ; 9(2): 106–118. doi:10.1038/nrneurol.2012.263	t	f	t	t	APOE4	1	2	0	I	\N	APOE
FDG	\N	t	t	t	t	FDG	\N	\N	\N	N	\N	PET
PIB	\N	t	t	t	t	PIB	\N	\N	\N	N	\N	PET
AV45	\N	t	t	t	t	AV45	\N	\N	\N	N	\N	brain_metabolism
CDRSB	Clinical dementia rate (sum of box)	t	t	t	t	CDRSB	\N	\N	\N	N	\N	cdrsb
ADAS11	ADAS SCORE based in 11 items	t	t	t	t	ADAS11	\N	\N	\N	N	\N	adas
ADAS13	ADAS SCORE based in 13 items (new version)	t	t	t	t	ADAS13	\N	\N	\N	N	\N	adas
MMSE	MINI-MENTAL STATE EXAM TOTAL SCORE is a global assessment of cognitive status (Folstein et al., 1975). The MMSE is a very brief, easily administered mental status examination that has proved to be a highly reliable and valid instrument for detecting and tracking the progression of the cognitive impairment associated with neurodegenerative diseases. Consequently, the MMSE is the most widely used mental status examination in the world. The MMSE is a fully structured scale that consists of 30 points grouped into seven categories. A perfect score is 30 points; a score of 24 is the recommended, and most frequently used, cutpoint for dementia; a score of 23 or lower indicates dementia. The MMSE takes only 5-10 minutes to administer and is therefore practical to use repeatedly and routinely.\n                                            See also\n                                            For Mini-Mental State Examination (MMSE) description see: http://www.ncbi.nlm.nih.gov/pmc/articles/PMC3638088/pdf/nihms454086.pdf	t	t	t	t	MMSE	\N	\N	\N	N	\N	mmse
RAVLT	\N	t	t	t	t	RAVLT	\N	\N	\N	N	\N	ravlt
RAVLT_immediate	\N	t	t	t	t	RAVLT_immediate	\N	\N	\N	N	\N	ravlt
FAQ	FUNCTIONAL ASSESSMENT QUESTIONNAIRE total score	t	t	t	t	FAQ	\N	\N	\N	N	\N	faq
MOCA	MONTREAL COGNITIVE ASSESMENT TOTAL SCORE	t	t	t	t	MOCA	\N	\N	\N	N	\N	moca
EcogPtMem	\N	t	t	t	t	EcogPtMem	\N	\N	\N	N	\N	ecog
EcogPtLang	\N	t	t	t	t	EcogPtLang	\N	\N	\N	N	\N	ecog
EcogPtVisspat	\N	t	t	t	t	EcogPtVisspat	\N	\N	\N	N	\N	ecog
EcogPtPlan	\N	t	t	t	t	EcogPtPlan	\N	\N	\N	N	\N	ecog
EcogPtOrgan	\N	t	t	t	t	EcogPtOrgan	\N	\N	\N	N	\N	ecog
EcogPtDivatt	\N	t	t	t	t	EcogPtDivatt	\N	\N	\N	N	\N	ecog
EcogPtTotal	\N	t	t	t	t	EcogPtTotal	\N	\N	\N	N	\N	ecog
EcogSPMem	\N	t	t	t	t	EcogSPMem	\N	\N	\N	N	\N	ecog
EcogSPLang	\N	t	t	t	t	EcogSPLang	\N	\N	\N	N	\N	ecog
EcogSPVisspat	\N	t	t	t	t	EcogSPVisspat	\N	\N	\N	N	\N	ecog
EcogSPPlan	\N	t	t	t	t	EcogSPPlan	\N	\N	\N	N	\N	ecog
EcogSPOrgan	\N	t	t	t	t	EcogSPOrgan	\N	\N	\N	N	\N	ecog
EcogSPDivatt	\N	t	t	t	t	EcogSPDivatt	\N	\N	\N	N	\N	ecog
EcogSPTotal	\N	t	t	t	t	EcogSPTotal	\N	\N	\N	N	\N	ecog
Ventricles	\N	t	t	t	t	Ventricles	\N	\N	\N	N	\N	ventricule
Hippocampus	\N	t	t	t	t	Hippocampus	\N	\N	\N	N	\N	grey_matter_surface
WholeBrain	\N	t	t	t	t	WholeBrain	\N	\N	\N	N	\N	grey_matter_surface
Entorhinal	\N	t	t	t	t	Entorhinal	\N	\N	\N	N	\N	limbic
Fusiform	\N	t	t	t	t	Fusiform	\N	\N	\N	N	\N	grey_matter_surface
MidTemp	\N	t	t	t	t	MidTemp	\N	\N	\N	N	\N	grey_matter_surface
ICV	\N	t	t	t	t	ICV	\N	\N	\N	N	\N	grey_matter_surface
DX	Diagnostic status based in clinical/cognitive evaluation (AD, MCI, NC)\n                                            Probable AD dementia (AD): is diagnosed when the patient has the following characteristics: A. Insidious onset. Symptoms have a gradual onset over months to years, not sudden over hours or days; B. Clear-cut history of worsening of cognition by report observation; and C. The initial and most prominent cognitive deficits are evident on history and examination in one of the following categories. a. Amnestic presentation b. Nonamnestic presentations: Language presentation, Visuospatial presentation, Executive dysfunctiont. D. The diagnosis of probable AD dementia should not be applied when there is evidence of (a) substantial concomitant cerebrovascular disease or the presence of multiple or extensive infarcts or severe white matter hyperintensity burden; or (b) core features of Dementia with Lewy bodies other than dementia itself; or (c) prominent features of behavioral variant frontotemporal dementia; or (d) prominent features of semantic variant primary progressive aphasia or non-fluent/agrammatic variant primary progressive aphasia; or (e) evidence for another concurrent, active neurological disease, or a non-neurological medical comorbidity or use of medication that could have a substantial effect on cognition.\n                                            Mild Cognitive Impairment (MCI):  is defined by a decline in cognitive functioning that is greater than would be expected for the patient's age and educational background and that goes beyond normal changes seen in ageing. This decline from a previous level can include a variety of cognitive domains, including learning and memory, complex attention, executive functions, language, perceptual-motor domain and social cognition , although it is common that decline manifests in a single domain only. Changes in cognitive functioning should be serious enough to be noticed either by the individuals experiencing them, by other people who know the patient well or by a skilled clinician in an appropriate clinical context.\n                                            Healthy Aging (NC): Healthy biological ageing includes survival to old age, delay in the onset of non-communicable diseases  and optimal functioning for the maximal period at individual levels (physical and cognitive capability), body systems and cells. “Health” refers to physical, mental and social well-being as expressed in the WHO definition of health. Maintaining autonomy and independence of elderly people is a key goal in the policy framework for active ageing.\n                                            See also:\n                                            Basic Concept of AD and 2015 World Alzheimer Report:\n                                            http://www.alz.org/alzheimers_disease_what_is_alzheimers.asp\n                                            http://www.alz.co.uk/research/WorldAlzheimerReport2015.pdf\n                                            See Mild cognitive impairment (MCI): http://www.alz.org/research/science/earlier_alzheimers_diagnosis.asp#Mild\n                                            Video: Identifying mild cognitive impairment (approx. 21 min.)\n                                            http://www.alz.org/research/video/video_pages/identifying_mild_cognitive_impairment.html\n                                            For National Institute on Aging and the Alzheimer's Association workgroup criteria (NINCDS/ADRDA) for probable AD see:  http://www.alz.org/documents_custom/Diagnostic_Recommendations_Alz_proof.pdf\n                                            For National Institute on Aging and the Alzheimer's Association workgroup criteria (NINCDS/ADRDA) for probable AD see:  http://www.alz.org/documents_custom/Diagnostic_Recommendations_Alz_proof.pdf\n                                            http://www.alz.org/alzheimers_disease_what_is_alzheimers.asp\n                                            For video:\n                                            http://www.alz.org/research/video/alzheimers_videos_and_media_understanding.asp	t	f	t	t	DX	\N	\N	\N	T	\N	diagnostic
EXAMDATE_bl	\N	t	t	t	t	EXAMDATE_bl	\N	\N	\N	D	\N	date
CDRSB_bl	Baseline Clinical dementia rate (sum of box)	t	t	t	t	CDRSB_bl	\N	\N	\N	N	\N	cdrsb
ADAS11_bl	Baseline ADAS SCORE based in 11 items	t	t	t	t	ADAS11_bl	\N	\N	\N	N	\N	adas
ADAS13_bl	Baseline ADAS SCORE based in 13 items (new version)	t	t	t	t	ADAS13_bl	\N	\N	\N	N	\N	adas
MMSE_bl	Baseline MINI-MENTAL STATE EXAM TOTAL SCORE	t	t	t	t	MMSE_bl	\N	\N	\N	N	\N	mmse
RAVLT_bl	REY AUDITORY VERBAL LEARNING TEST	t	t	t	t	RAVLT_bl	\N	\N	\N	N	\N	ravlt
RAVLT_immediate_bl	REY AUDITORY VERBAL LEARNING TEST	t	t	t	t	RAVLT_immediate_bl	\N	\N	\N	N	\N	ravlt
FAQ_bl	Baseline FUNCTIONAL ASSESSMENT QUESTIONNAIRE total score	t	t	t	t	FAQ_bl	\N	\N	\N	N	\N	faq
Ventricles_bl	\N	t	t	t	t	Ventricles_bl	\N	\N	\N	N	\N	ventricule
Hippocampus_bl	\N	t	t	t	t	Hippocampus_bl	\N	\N	\N	N	\N	grey_matter_surface
WholeBrain_bl	\N	t	t	t	t	WholeBrain_bl	\N	\N	\N	N	\N	grey_matter_surface
Entorhinal_bl	\N	t	t	t	t	Entorhinal_bl	\N	\N	\N	N	\N	grey_matter_surface
Fusiform_bl	\N	t	t	t	t	Fusiform_bl	\N	\N	\N	N	\N	grey_matter_surface
MidTemp_bl	\N	t	t	t	t	MidTemp_bl	\N	\N	\N	N	\N	grey_matter_surface
ICV_bl	\N	t	t	t	t	ICV_bl	\N	\N	\N	N	\N	grey_matter_surface
MOCA_bl	Baseline MONTREAL COGNITIVE ASSESMENT TOTAL SCORE	t	t	t	t	MOCA_bl	\N	\N	\N	N	\N	moca
EcogPtMem_bl	\N	t	t	t	t	EcogPtMem_bl	\N	\N	\N	N	\N	ecog
EcogPtLang_bl	\N	t	t	t	t	EcogPtLang_bl	\N	\N	\N	N	\N	ecog
EcogPtVisspat_bl	\N	t	t	t	t	EcogPtVisspat_bl	\N	\N	\N	N	\N	ecog
EcogPtPlan_bl	\N	t	t	t	t	EcogPtPlan_bl	\N	\N	\N	N	\N	ecog
EcogPtOrgan_bl	\N	t	t	t	t	EcogPtOrgan_bl	\N	\N	\N	N	\N	ecog
EcogPtDivatt_bl	\N	t	t	t	t	EcogPtDivatt_bl	\N	\N	\N	N	\N	ecog
EcogPtTotal_bl	\N	t	t	t	t	EcogPtTotal_bl	\N	\N	\N	N	\N	ecog
EcogSPMem_bl	\N	t	t	t	t	EcogSPMem_bl	\N	\N	\N	N	\N	ecog
EcogSPLang_bl	\N	t	t	t	t	EcogSPLang_bl	\N	\N	\N	N	\N	ecog
EcogSPVisspat_bl	\N	t	t	t	t	EcogSPVisspat_bl	\N	\N	\N	N	\N	ecog
EcogSPPlan_bl	\N	t	t	t	t	EcogSPPlan_bl	\N	\N	\N	N	\N	ecog
EcogSPOrgan_bl	\N	t	t	t	t	EcogSPOrgan_bl	\N	\N	\N	N	\N	ecog
EcogSPDivatt_bl	\N	t	t	t	t	EcogSPDivatt_bl	\N	\N	\N	N	\N	ecog
EcogSPTotal_bl	\N	t	t	t	t	EcogSPTotal_bl	\N	\N	\N	N	\N	ecog
FDG_bl	\N	t	t	t	t	FDG_bl	\N	\N	\N	N	\N	PET
PIB_bl	\N	t	t	t	t	PIB_bl	\N	\N	\N	N	\N	PET
AV45_bl	\N	t	t	t	t	AV45_bl	\N	\N	\N	N	\N	PET
Years_bl	\N	t	t	t	t	Years_bl	\N	\N	\N	N	\N	year
Month_bl	\N	t	t	t	t	Month_bl	\N	\N	\N	N	\N	no-group
Month	\N	t	t	t	t	Month	\N	\N	\N	N	\N	no-group
ScanDate	\N	t	t	t	t	ScanDate	10	\N	\N	D	\N	date
VisitCode	\N	f	f	f	t	VisitCode	\N	\N	\N	T	\N	date
VisitNumber	\N	t	f	t	t	VisitNumber	\N	\N	\N	I	\N	number
TotalNumberofVisits	\N	t	f	t	t	TotalNumberofVisits	\N	\N	\N	I	\N	number
PeriodTime	\N	t	t	t	t	PeriodTime	\N	\N	\N	N	\N	period
MRIScanner	\N	t	f	t	t	MRIScanner	\N	\N	\N	N	\N	machine
3rdVentricle	\N	t	t	t	t	3rdVentricle	20	\N	\N	N	cm3	ventricule
4thVentricle	\N	t	t	t	t	4thVentricle	20	\N	\N	N	cm3	ventricule
RightAccumbensArea	\N	t	t	t	t	RightAccumbensArea	20	\N	\N	N	cm3	basal_ganglia
LeftAccumbensArea	\N	t	t	t	t	LeftAccumbensArea	20	\N	\N	N	cm3	basal_ganglia
RightAmygdala	\N	t	t	t	t	RightAmygdala	20	\N	\N	N	cm3	amygdala
LeftAmygdala	\N	t	t	t	t	LeftAmygdala	20	\N	\N	N	cm3	amygdala
BrainStem	\N	t	t	t	t	BrainStem	20	\N	\N	N	cm3	BrainStem
RightCaudate	\N	t	t	t	t	RightCaudate	20	\N	\N	N	cm3	basal_ganglia
LeftCaudate	\N	t	t	t	t	LeftCaudate	20	\N	\N	N	cm3	basal_ganglia
RightCerebellumExterior	\N	t	t	t	t	RightCerebellumExterior	20	\N	\N	N	cm3	grey_matter_surface
LeftCerebellumExterior	\N	t	t	t	t	LeftCerebellumExterior	20	\N	\N	N	cm3	grey_matter_surface
RightCerebellumWhiteMatter	\N	t	t	t	t	RightCerebellumWhiteMatter	20	\N	\N	N	cm3	CerebellumWhiteMatter
LeftCerebellumWhiteMatter	\N	t	t	t	t	LeftCerebellumWhiteMatter	20	\N	\N	N	cm3	CerebellumWhiteMatter
RightCerebralWhiteMatter	\N	t	t	t	t	RightCerebralWhiteMatter	20	\N	\N	N	cm3	GlobalWhiteMatter
LeftCerebralWhiteMatter	\N	t	t	t	t	LeftCerebralWhiteMatter	20	\N	\N	N	cm3	GlobalWhiteMatter
CSF	\N	t	t	t	t	CSF	20	\N	\N	N	cm3	GlobalWhiteMatter
RightHippocampus	\N	t	t	t	t	RightHippocampus	20	\N	\N	N	cm3	grey_matter_surface
LeftHippocampus	\N	t	t	t	t	LeftHippocampus	20	\N	\N	N	cm3	grey_matter_surface
RightInfLatVent	\N	t	t	t	t	RightInfLatVent	20	\N	\N	N	cm3	ventricule
LeftInfLatVent	\N	t	t	t	t	LeftInfLatVent	20	\N	\N	N	cm3	ventricule
RightLateralVentricle	\N	t	t	t	t	RightLateralVentricle	20	\N	\N	N	cm3	ventricule
LeftLateralVentricle	\N	t	t	t	t	LeftLateralVentricle	20	\N	\N	N	cm3	ventricule
RightPallidum	\N	t	t	t	t	RightPallidum	20	\N	\N	N	cm3	basal_ganglia
LeftPallidum	\N	t	t	t	t	LeftPallidum	20	\N	\N	N	cm3	basal_ganglia
RightPutamen	\N	t	t	t	t	RightPutamen	20	\N	\N	N	cm3	basal_ganglia
LeftPutamen	\N	t	t	t	t	LeftPutamen	20	\N	\N	N	cm3	basal_ganglia
RightThalamusProper	\N	t	t	t	t	RightThalamusProper	20	\N	\N	N	cm3	grey_matter_surface
LeftThalamusProper	\N	t	t	t	t	LeftThalamusProper	20	\N	\N	N	cm3	grey_matter_surface
RightVentralDC	\N	t	t	t	t	RightVentralDC	20	\N	\N	N	cm3	diencephalon
LeftVentralDC	\N	t	t	t	t	LeftVentralDC	20	\N	\N	N	cm3	diencephalon
Rightvessel	\N	t	t	t	t	Rightvessel	20	\N	\N	N	cm3	vessel
Leftvessel	\N	t	t	t	t	Leftvessel	20	\N	\N	N	cm3	vessel
OpticChiasm	\N	t	t	t	t	OpticChiasm	20	\N	\N	N	cm3	OpticChiasmWhiteMatter
CerebellarVermalLobulesI-V	\N	t	t	t	t	CerebellarVermalLobulesI-V	20	\N	\N	N	cm3	grey_matter_surface
CerebellarVermalLobulesVI-VII	\N	t	t	t	t	CerebellarVermalLobulesVI-VII	20	\N	\N	N	cm3	grey_matter_surface
CerebellarVermalLobulesVIII-X	\N	t	t	t	t	CerebellarVermalLobulesVIII-X	20	\N	\N	N	cm3	grey_matter_surface
LeftBasalForebrain	\N	t	t	t	t	LeftBasalForebrain	20	\N	\N	N	cm3	basal_forebrain
RightBasalForebrain	\N	t	t	t	t	RightBasalForebrain	20	\N	\N	N	cm3	basal_forebrain
RightACgGanteriorcingulategyrus	\N	t	t	t	t	RightACgGanteriorcingulategyrus	20	\N	\N	N	cm3	limbic
LeftACgGanteriorcingulategyrus	\N	t	t	t	t	LeftACgGanteriorcingulategyrus	20	\N	\N	N	cm3	limbic
RightAInsanteriorinsula	\N	t	t	t	t	RightAInsanteriorinsula	20	\N	\N	N	cm3	insula
LeftAInsanteriorinsula	\N	t	t	t	t	LeftAInsanteriorinsula	20	\N	\N	N	cm3	insula
RightAOrGanteriororbitalgyrus	\N	t	t	t	t	RightAOrGanteriororbitalgyrus	20	\N	\N	N	cm3	frontal
LeftAOrGanteriororbitalgyrus	\N	t	t	t	t	LeftAOrGanteriororbitalgyrus	20	\N	\N	N	cm3	frontal
RightAnGangulargyrus	\N	t	t	t	t	RightAnGangulargyrus	20	\N	\N	N	cm3	parietal
LeftAnGangulargyrus	\N	t	t	t	t	LeftAnGangulargyrus	20	\N	\N	N	cm3	parietal
RightCalccalcarinecortex	\N	t	t	t	t	RightCalccalcarinecortex	20	\N	\N	N	cm3	occipital
LeftCalccalcarinecortex	\N	t	t	t	t	LeftCalccalcarinecortex	20	\N	\N	N	cm3	occipital
RightCOcentraloperculum	\N	t	t	t	t	RightCOcentraloperculum	20	\N	\N	N	cm3	frontal
LeftCOcentraloperculum	\N	t	t	t	t	LeftCOcentraloperculum	20	\N	\N	N	cm3	frontal
RightCuncuneus	\N	t	t	t	t	RightCuncuneus	20	\N	\N	N	cm3	occipital
LeftCuncuneus	\N	t	t	t	t	LeftCuncuneus	20	\N	\N	N	cm3	occipital
RightEntentorhinalarea	\N	t	t	t	t	RightEntentorhinalarea	20	\N	\N	N	cm3	limbic
LeftEntentorhinalarea	\N	t	t	t	t	LeftEntentorhinalarea	20	\N	\N	N	cm3	limbic
RightFOfrontaloperculum	\N	t	t	t	t	RightFOfrontaloperculum	20	\N	\N	N	cm3	frontal
LeftFOfrontaloperculum	\N	t	t	t	t	LeftFOfrontaloperculum	20	\N	\N	N	cm3	frontal
RightFRPfrontalpole	\N	t	t	t	t	RightFRPfrontalpole	20	\N	\N	N	cm3	frontal
LeftFRPfrontalpole	\N	t	t	t	t	LeftFRPfrontalpole	20	\N	\N	N	cm3	frontal
RightFuGfusiformgyrus	\N	t	t	t	t	RightFuGfusiformgyrus	20	\N	\N	N	cm3	temporal
LeftFuGfusiformgyrus	\N	t	t	t	t	LeftFuGfusiformgyrus	20	\N	\N	N	cm3	temporal
RightGRegyrusrectus	\N	t	t	t	t	RightGRegyrusrectus	20	\N	\N	N	cm3	frontal
LeftGRegyrusrectus	\N	t	t	t	t	LeftGRegyrusrectus	20	\N	\N	N	cm3	frontal
RightIOGinferioroccipitalgyrus	\N	t	t	t	t	RightIOGinferioroccipitalgyrus	20	\N	\N	N	cm3	occipital
LeftIOGinferioroccipitalgyrus	\N	t	t	t	t	LeftIOGinferioroccipitalgyrus	20	\N	\N	N	cm3	occipital
RightITGinferiortemporalgyrus	\N	t	t	t	t	RightITGinferiortemporalgyrus	20	\N	\N	N	cm3	temporal
LeftITGinferiortemporalgyrus	\N	t	t	t	t	LeftITGinferiortemporalgyrus	20	\N	\N	N	cm3	temporal
RightLiGlingualgyrus	\N	t	t	t	t	RightLiGlingualgyrus	20	\N	\N	N	cm3	occipital
LeftLiGlingualgyrus	\N	t	t	t	t	LeftLiGlingualgyrus	20	\N	\N	N	cm3	occipital
RightLOrGlateralorbitalgyrus	\N	t	t	t	t	RightLOrGlateralorbitalgyrus	20	\N	\N	N	cm3	frontal
LeftLOrGlateralorbitalgyrus	\N	t	t	t	t	LeftLOrGlateralorbitalgyrus	20	\N	\N	N	cm3	frontal
RightMCgGmiddlecingulategyrus	\N	t	t	t	t	RightMCgGmiddlecingulategyrus	20	\N	\N	N	cm3	limbic
LeftMCgGmiddlecingulategyrus	\N	t	t	t	t	LeftMCgGmiddlecingulategyrus	20	\N	\N	N	cm3	limbic
RightMFCmedialfrontalcortex	\N	t	t	t	t	RightMFCmedialfrontalcortex	20	\N	\N	N	cm3	frontal
LeftMFCmedialfrontalcortex	\N	t	t	t	t	LeftMFCmedialfrontalcortex	20	\N	\N	N	cm3	frontal
RightMFGmiddlefrontalgyrus	\N	t	t	t	t	RightMFGmiddlefrontalgyrus	20	\N	\N	N	cm3	frontal
LeftMFGmiddlefrontalgyrus	\N	t	t	t	t	LeftMFGmiddlefrontalgyrus	20	\N	\N	N	cm3	frontal
RightMOGmiddleoccipitalgyrus	\N	t	t	t	t	RightMOGmiddleoccipitalgyrus	20	\N	\N	N	cm3	occipital
LeftMOGmiddleoccipitalgyrus	\N	t	t	t	t	LeftMOGmiddleoccipitalgyrus	20	\N	\N	N	cm3	occipital
RightMOrGmedialorbitalgyrus	\N	t	t	t	t	RightMOrGmedialorbitalgyrus	20	\N	\N	N	cm3	frontal
LeftMOrGmedialorbitalgyrus	\N	t	t	t	t	LeftMOrGmedialorbitalgyrus	20	\N	\N	N	cm3	frontal
RightMPoGpostcentralgyrusmedialsegment	\N	t	t	t	t	RightMPoGpostcentralgyrusmedialsegment	20	\N	\N	N	cm3	parietal
LeftMPoGpostcentralgyrusmedialsegment	\N	t	t	t	t	LeftMPoGpostcentralgyrusmedialsegment	20	\N	\N	N	cm3	parietal
RightMPrGprecentralgyrusmedialsegment	\N	t	t	t	t	RightMPrGprecentralgyrusmedialsegment	20	\N	\N	N	cm3	frontal
LeftMPrGprecentralgyrusmedialsegment	\N	t	t	t	t	LeftMPrGprecentralgyrusmedialsegment	20	\N	\N	N	cm3	frontal
RightMSFGsuperiorfrontalgyrusmedialsegment	\N	t	t	t	t	RightMSFGsuperiorfrontalgyrusmedialsegment	20	\N	\N	N	cm3	frontal
LeftMSFGsuperiorfrontalgyrusmedialsegment	\N	t	t	t	t	LeftMSFGsuperiorfrontalgyrusmedialsegment	20	\N	\N	N	cm3	frontal
RightMTGmiddletemporalgyrus	\N	t	t	t	t	RightMTGmiddletemporalgyrus	20	\N	\N	N	cm3	temporal
LeftMTGmiddletemporalgyrus	\N	t	t	t	t	LeftMTGmiddletemporalgyrus	20	\N	\N	N	cm3	temporal
RightOCPoccipitalpole	\N	t	t	t	t	RightOCPoccipitalpole	20	\N	\N	N	cm3	occipital
LeftOCPoccipitalpole	\N	t	t	t	t	LeftOCPoccipitalpole	20	\N	\N	N	cm3	occipital
RightOFuGoccipitalfusiformgyrus	\N	t	t	t	t	RightOFuGoccipitalfusiformgyrus	20	\N	\N	N	cm3	occipital
LeftOFuGoccipitalfusiformgyrus	\N	t	t	t	t	LeftOFuGoccipitalfusiformgyrus	20	\N	\N	N	cm3	occipital
RightOpIFGopercularpartoftheinferiorfrontalgyrus	\N	t	t	t	t	RightOpIFGopercularpartoftheinferiorfrontalgyrus	20	\N	\N	N	cm3	frontal
LeftOpIFGopercularpartoftheinferiorfrontalgyrus	\N	t	t	t	t	LeftOpIFGopercularpartoftheinferiorfrontalgyrus	20	\N	\N	N	cm3	frontal
RightOrIFGorbitalpartoftheinferiorfrontalgyrus	\N	t	t	t	t	RightOrIFGorbitalpartoftheinferiorfrontalgyrus	20	\N	\N	N	cm3	frontal
LeftOrIFGorbitalpartoftheinferiorfrontalgyrus	\N	t	t	t	t	LeftOrIFGorbitalpartoftheinferiorfrontalgyrus	20	\N	\N	N	cm3	frontal
RightPCgGposteriorcingulategyrus	\N	t	t	t	t	RightPCgGposteriorcingulategyrus	20	\N	\N	N	cm3	limbic
LeftPCgGposteriorcingulategyrus	\N	t	t	t	t	LeftPCgGposteriorcingulategyrus	20	\N	\N	N	cm3	limbic
RightPCuprecuneus	\N	t	t	t	t	RightPCuprecuneus	20	\N	\N	N	cm3	parietal
LeftPCuprecuneus	\N	t	t	t	t	LeftPCuprecuneus	20	\N	\N	N	cm3	parietal
RightPHGparahippocampalgyrus	\N	t	t	t	t	RightPHGparahippocampalgyrus	20	\N	\N	N	cm3	limbic
LeftPHGparahippocampalgyrus	\N	t	t	t	t	LeftPHGparahippocampalgyrus	20	\N	\N	N	cm3	limbic
RightPInsposteriorinsula	\N	t	t	t	t	RightPInsposteriorinsula	20	\N	\N	N	cm3	insula
LeftPInsposteriorinsula	\N	t	t	t	t	LeftPInsposteriorinsula	20	\N	\N	N	cm3	insula
RightPOparietaloperculum	\N	t	t	t	t	RightPOparietaloperculum	20	\N	\N	N	cm3	frontal
LeftPOparietaloperculum	\N	t	t	t	t	LeftPOparietaloperculum	20	\N	\N	N	cm3	frontal
RightPoGpostcentralgyrus	\N	t	t	t	t	RightPoGpostcentralgyrus	20	\N	\N	N	cm3	parietal
LeftPoGpostcentralgyrus	\N	t	t	t	t	LeftPoGpostcentralgyrus	20	\N	\N	N	cm3	parietal
RightPOrGposteriororbitalgyrus	\N	t	t	t	t	RightPOrGposteriororbitalgyrus	20	\N	\N	N	cm3	frontal
LeftPOrGposteriororbitalgyrus	\N	t	t	t	t	LeftPOrGposteriororbitalgyrus	20	\N	\N	N	cm3	frontal
RightPPplanumpolare	\N	t	t	t	t	RightPPplanumpolare	20	\N	\N	N	cm3	temporal
LeftPPplanumpolare	\N	t	t	t	t	LeftPPplanumpolare	20	\N	\N	N	cm3	temporal
RightPrGprecentralgyrus	\N	t	t	t	t	RightPrGprecentralgyrus	20	\N	\N	N	cm3	frontal
LeftPrGprecentralgyrus	\N	t	t	t	t	LeftPrGprecentralgyrus	20	\N	\N	N	cm3	frontal
RightPTplanumtemporale	\N	t	t	t	t	RightPTplanumtemporale	20	\N	\N	N	cm3	temporal
LeftPTplanumtemporale	\N	t	t	t	t	LeftPTplanumtemporale	20	\N	\N	N	cm3	temporal
RightSCAsubcallosalarea	\N	t	t	t	t	RightSCAsubcallosalarea	20	\N	\N	N	cm3	frontal
LeftSCAsubcallosalarea	\N	t	t	t	t	LeftSCAsubcallosalarea	20	\N	\N	N	cm3	frontal
RightSFGsuperiorfrontalgyrus	\N	t	t	t	t	RightSFGsuperiorfrontalgyrus	20	\N	\N	N	cm3	frontal
LeftSFGsuperiorfrontalgyrus	\N	t	t	t	t	LeftSFGsuperiorfrontalgyrus	20	\N	\N	N	cm3	frontal
RightSMCsupplementarymotorcortex	\N	t	t	t	t	RightSMCsupplementarymotorcortex	20	\N	\N	N	cm3	frontal
LeftSMCsupplementarymotorcortex	\N	t	t	t	t	LeftSMCsupplementarymotorcortex	20	\N	\N	N	cm3	frontal
RightSMGsupramarginalgyrus	\N	t	t	t	t	RightSMGsupramarginalgyrus	20	\N	\N	N	cm3	parietal
LeftSMGsupramarginalgyrus	\N	t	t	t	t	LeftSMGsupramarginalgyrus	20	\N	\N	N	cm3	parietal
RightSOGsuperioroccipitalgyrus	\N	t	t	t	t	RightSOGsuperioroccipitalgyrus	20	\N	\N	N	cm3	occipital
LeftSOGsuperioroccipitalgyrus	\N	t	t	t	t	LeftSOGsuperioroccipitalgyrus	20	\N	\N	N	cm3	occipital
RightSPLsuperiorparietallobule	\N	t	t	t	t	RightSPLsuperiorparietallobule	20	\N	\N	N	cm3	parietal
LeftSPLsuperiorparietallobule	\N	t	t	t	t	LeftSPLsuperiorparietallobule	20	\N	\N	N	cm3	parietal
RightSTGsuperiortemporalgyrus	\N	t	t	t	t	RightSTGsuperiortemporalgyrus	20	\N	\N	N	cm3	temporal
LeftSTGsuperiortemporalgyrus	\N	t	t	t	t	LeftSTGsuperiortemporalgyrus	20	\N	\N	N	cm3	temporal
RightTMPtemporalpole	\N	t	t	t	t	RightTMPtemporalpole	20	\N	\N	N	cm3	temporal
LeftTMPtemporalpole	\N	t	t	t	t	LeftTMPtemporalpole	20	\N	\N	N	cm3	temporal
RightTrIFGtriangularpartoftheinferiorfrontalgyrus	\N	t	t	t	t	RightTrIFGtriangularpartoftheinferiorfrontalgyrus	20	\N	\N	N	cm3	frontal
LeftTrIFGtriangularpartoftheinferiorfrontalgyrus	\N	t	t	t	t	LeftTrIFGtriangularpartoftheinferiorfrontalgyrus	20	\N	\N	N	cm3	frontal
RightTTGtransversetemporalgyrus	\N	t	t	t	t	RightTTGtransversetemporalgyrus	20	\N	\N	N	cm3	temporal
LeftTTGtransversetemporalgyrus	\N	t	t	t	t	LeftTTGtransversetemporalgyrus	20	\N	\N	N	cm3	temporal
\.

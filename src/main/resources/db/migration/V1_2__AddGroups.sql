--
-- Add group
--

COPY "group" (code, label, parent_code) FROM stdin;
root	root	\N
no-group	no-group	root
provenance	provenance	root
source	source	provenance
protocol	protocol	provenance
date	date	provenance
brain_metabolism	brain_metabolism	root
PET	PET	brain_metabolism
brain_scan	brain_scan	root
machine	machine	brain_scan
clinical	clinical	root
diagnostic	diagnostic	clinical
cognition	cognition	clinical
ecog	ECoG	cognition
adas	ADAS	cognition
mmse	MMSE	cognition
ravlt	RAVLT	cognition
moca	MOCA	cognition
faq	FAQ	cognition
cdrsb	CDRSB	cognition
Date	Date	root
year	year	Date
demographic	demographic	root
age	age	demographic
genre	genre	demographic
education	education	demographic
ethny	ethny	demographic
country	country	demographic
marital	marital	demographic
genetic	Genetic	root
polymorphism	polymorphism	genetic
APOE	APOE	polymorphism
visit	visit	root
number	number	visit
period	period	visit
brain	Brain	root
brain_anatomy	Brain anatomy	brain
grey_matter_volume	Grey matter volume	brain_anatomy
insula	Insula	grey_matter_volume
frontal	Frontal	grey_matter_volume
orbitalgyrus	Orbitalgyrus	frontal
parietal	Parietal	grey_matter_volume
occipital	Occipital	grey_matter_volume
temporal	Temporal	grey_matter_volume
limbic	Limbic	grey_matter_volume
cerebral_nuclei	Cerebral nuclei	grey_matter_volume
basal_ganglia	Basal Ganglia	cerebral_nuclei
amygdala	Amygdala	cerebral_nuclei
basal_forebrain	Basal forebrain	cerebral_nuclei
csf	CSF	brain_anatomy
ventricule	Ventricule	csf
white_matter_volume	White matter volume	brain_anatomy
BrainStem	Brain stem	white_matter_volume
GlobalWhiteMatter	Global	white_matter_volume
CerebellumWhiteMatter	Cerebellum	white_matter_volume
OpticChiasmWhiteMatter	OpticChiasm	white_matter_volume
diencephalon	Diencephalon	white_matter_volume
vessel	Vessel	white_matter_volume
grey_matter_surface	Grey matter surface	brain
\.


--
-- Add group_group
--

COPY group_group (group_code, groups_code) FROM stdin;
provenance	source
provenance	protocol
provenance	date
brain_metabolism	PET
brain_scan	machine
cognition	ecog
cognition	adas
cognition	mmse
cognition	ravlt
cognition	moca
cognition	faq
cognition	cdrsb
clinical	diagnostic
clinical	cognition
Date	year
demographic	age
demographic	genre
demographic	education
demographic	ethny
demographic	country
demographic	marital
polymorphism	APOE
genetic	polymorphism
visit	number
visit	period
frontal	orbitalgyrus
cerebral_nuclei	basal_ganglia
cerebral_nuclei	amygdala
cerebral_nuclei	basal_forebrain
grey_matter_volume	insula
grey_matter_volume	frontal
grey_matter_volume	parietal
grey_matter_volume	occipital
grey_matter_volume	temporal
grey_matter_volume	limbic
grey_matter_volume	cerebral_nuclei
csf	ventricule
white_matter_volume	BrainStem
white_matter_volume	GlobalWhiteMatter
white_matter_volume	CerebellumWhiteMatter
white_matter_volume	OpticChiasmWhiteMatter
white_matter_volume	diencephalon
white_matter_volume	vessel
brain_anatomy	grey_matter_volume
brain_anatomy	csf
brain_anatomy	white_matter_volume
brain	brain_anatomy
brain	grey_matter_surface
root	no-group
root	provenance
root	brain_metabolism
root	brain_scan
root	clinical
root	Date
root	demographic
root	genetic
root	visit
root	brain
\.

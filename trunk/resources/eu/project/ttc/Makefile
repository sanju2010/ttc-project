#!/usr/bin/make -f

source=French
target=Latvian

src=$(shell echo $(source) | tr A-Z a-z)
tgt=$(shell echo $(target) | tr A-Z a-z)

code=lv
srcCode=fr
tgtCode=$(code)

files=AcabitAnalysisEngine.xml
files+=CompoundSplitter.xml
files+=LemmaFilter.xml
files+=MultiWordTermDetector.xml
files+=NeoClassicalAdjectiveDetector.xml
files+=NeoClassicalNounDetector.xml
files+=NeoClassicalTermDetector.xml
files+=Stemmer.xml
files+=TermGatherer.xml
files+=TermDetector.xml
files+=TermFilter.xml
files+=TermIndexer.xml
files+=TreeTaggerCategoryNormalizer.xml
files+=TreeTaggerSubcategoryNormalizer.xml
files+=TreeTaggerMoodNormalizer.xml
files+=TreeTaggerTenseNormalizer.xml
files+=TreeTaggerWrapper.xml
files+=TreeTaggerAnalysisEngine.xml
files+=WordTokenizer.xml

resources =category-mapping.xml 
resources+=mood-mapping.xml
resources+=multi-word-rule-system.xml
resources+=segment-bank.xml
resources+=stop-word-filter.xml
resources+=subcategory-mapping.xml
resources+=tense-mapping.xml
resources+=variant-rule-system.xml

sources = $(addprefix $(src)/engines/$(source),$(files))
targets = $(addprefix $(tgt)/engines/$(target),$(files))

sourceResources = $(addprefix $(src)/resources/$(src)-,$(resources))
targetResources = $(addprefix $(tgt)/resources/$(tgt)-,$(resources))

all: copy-files

copy-files: $(sources)
	@for file in $(files) ; do make -s copy-file file=$$file ; done

copy-file: $(addprefix $(src)/engines/$(source),$(file))
	@cp $(addprefix $(src)/engines/$(source),$(file)) $(addprefix $(tgt)/engines/$(target),$(file))
	@make -s edit-file file=$(file)

edit-file: $(addprefix $(tgt)/engines/$(target),$(file))
	@sed -i 's/$(source)/$(target)/g' $(addprefix $(tgt)/engines/$(target),$(file))
	@sed -i 's/$(src)/$(tgt)/g' $(addprefix $(tgt)/engines/$(target),$(file))
	@sed -i 's/language>$(srcCode)/language>$(tgtCode)/g' $(addprefix $(tgt)/engines/$(target),$(file))
	@sed -i 's/string>$(srcCode)/string>$(tgtCode)/g' $(addprefix $(tgt)/engines/$(target),$(file))

copy-resources: $(sourceResources)
	@for file in $(resources) ; do make -s copy-resource resource=$$file ; done

copy-resource: $(addprefix $(src)/resources/$(src)-,$(resource))
	@cp $(addprefix $(src)/resources/$(src)-,$(resource)) $(addprefix $(tgt)/resources/$(tgt)-,$(resource))

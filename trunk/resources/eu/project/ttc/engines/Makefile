#!/usr/bin/make -f

source=French
target=English

src=$(shell echo $(source) | tr A-Z a-z)
tgt=$(shell echo $(target) | tr A-Z a-z)

files=LinguisticAnalysis.xml
files+=NeoClassicalTermDetector.xml
files+=TreeTaggerMultextMapper.xml
files+=NeoClassicalAdjectiveDetector.xml
files+=RuleBasedTermDetector.xml
files+=TreeTaggerWrapper.xml
files+=NeoClassicalNounDetector.xml
files+=TermDetector.xml
files+=WordTokenizer.xml

sources = $(addprefix $(src)/$(source),$(files))
targets = $(addprefix $(tgt)/$(target),$(files))

all: copy-files

copy-files: $(sources)
	@for file in $(files) ; do make -s copy-file file=$$file ; done

copy-file: $(addprefix $(src)/$(source),$(file))
	@cp $(addprefix $(src)/$(source),$(file)) $(addprefix $(tgt)/$(target),$(file))
	@make -s edit-file file=$(file)

edit-file: $(addprefix $(tgt)/$(target),$(file))
	@sed -i 's/$(source)/$(target)/' $(addprefix $(tgt)/$(target),$(file))
	@sed -i 's/$(src)/$(tgt)/' $(addprefix $(tgt)/$(target),$(file))

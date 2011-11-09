#!/usr/bin/make -f

source=French
target=English

src=$(shell echo $(source) | tr A-Z a-z)
tgt=$(shell echo $(target) | tr A-Z a-z)

code=en
srcCode=fr
tgtCode=$(code)

files=Acabit.xml
files+=MultiWordTermDetector.xml
files+=NeoClassicalAdjectiveDetector.xml
files+=NeoClassicalNounDetector.xml
files+=NeoClassicalTermDetector.xml
files+=TermDetector.xml
files+=TermFilter.xml
files+=TermIndexer.xml
files+=TreeTaggerCategoryNormalizer.xml
files+=TreeTaggerWrapper.xml
files+=TreeTagger.xml
files+=WordTokenizer.xml

sources = $(addprefix $(src)/engines/$(source),$(files))
targets = $(addprefix $(tgt)/engines/$(target),$(files))

all: copy-files

copy-files: $(sources)
	@for file in $(files) ; do make -s copy-file file=$$file ; done

copy-file: $(addprefix $(src)/engines/$(source),$(file))
	@cp $(addprefix $(src)/engines/$(source),$(file)) $(addprefix $(tgt)/engines/$(target),$(file))
	@make -s edit-file file=$(file)

edit-file: $(addprefix $(tgt)/engines/$(target),$(file))
	@sed -i 's/$(source)/$(target)/' $(addprefix $(tgt)/engines/$(target),$(file))
	@sed -i 's/$(src)/$(tgt)/' $(addprefix $(tgt)/engines/$(target),$(file))
	@sed -i 's/language>$(srcCode)/language>$(tgtCode)/' $(addprefix $(tgt)/engines/$(target),$(file))


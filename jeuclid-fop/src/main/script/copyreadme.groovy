def source = new File("src/main/resources/README.fop")
def target = new File("src/site/apt/index.apt")

ant.mkdir(dir: target.getParent())
ant.copy(file: source, tofile: target)

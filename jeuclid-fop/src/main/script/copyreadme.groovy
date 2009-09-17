def source = new File(project.basedir,"src/main/resources/net/sourceforge/jeuclid/README.fop")
def target = new File(project.basedir,"src/site/apt/index.apt")

ant.mkdir(dir: target.getParent())
ant.copy(file: source, tofile: target)

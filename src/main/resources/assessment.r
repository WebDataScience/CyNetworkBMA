try(
{
	suppressPackageStartupMessages(library(networkBMA))

	contabs <- contabs.netwBMA(edgeList, referencePairs)
	scores <- scores(contabs)
	scores.100 <- scores(contabs.netwBMA(edgeList, referencePairs, thresh=seq(from=0, to=1, by=0.01)))

	if (suppressWarnings(require('Cairo', quietly=TRUE))) {
		device <- CairoPNG
	} else {
		device <- png
	}

	device(file="roc.png", width=480, height=360)
	auroc <- roc(contabs, plotit = TRUE)["area"]
	title(paste("AUC:", auroc))
	dev.off()
	roc <- readBin("roc.png", 'raw', 1024*1024)
	unlink("roc.png")

	device(file="prc.png", width=480, height=360)
	auprc <- prc(contabs, plotit = TRUE)["area"]
	title(paste("AUC:", auprc))
	dev.off()
	prc <- readBin("prc.png", 'raw', 1024*1024)
	unlink("prc.png")
}, silent=TRUE)

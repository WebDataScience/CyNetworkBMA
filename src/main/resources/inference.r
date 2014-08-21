try(
{
	suppressPackageStartupMessages(library(networkBMA))
	suppressPackageStartupMessages(library(BMA))
	suppressPackageStartupMessages(library(igraph))

	genes <- sapply(genes, gsub, pattern = "-", replacement = ".")
	colnames(data) <- genes

	if (algorithm == "ScanBMA") {
		gCtrl <- gControl(
			optimize = optimize,
			g0 = g0,
			iterlim = iterlim,
			epsilon = epsilon)
		control <- ScanBMAcontrol(
			OR = OR,
			useg = useg,
			gCtrl = gCtrl,
			thresProbne0 = thresProbne0)
	} else if (algorithm == "iBMA") {
		control <- iBMAcontrolLM(
			OR = OR,
			nbest = nbest,
			maxNvar = maxNvar,
			thresProbne0 = thresProbne0,
			keepModels = keepModels,
			maxIter = maxIter)
	}

	if (!is.null(prior.prob.const)) {
		prob <- prior.prob.const
	} else if (!is.null(prior.prob.matrix)) {
		prob <- prior.prob.matrix
		rownames(prob) <- prior.prob.regulator.labels
		colnames(prob) <- prior.prob.regulated.labels
	} else {
		prob <- NULL;
	}

	if (isTimeSeries) {
		edge.list <- networkBMA(data, nTimePoints, prior.prob = prob, ordering = ordering, nvar = nvar, control = control)
		out <- edge.list[as.numeric(edge.list[, 3]) >= postProbThreshold/100, ]
	} else {
		mvar <- ncol(data) - 1
		nvar <- if (is.null(nvar)) as.numeric(mvar) else min(mvar,nvar)
		bicreg.nbest <- 10
		bicreg.maxNvar <- 30
		
		edge.list <- NULL
		
		for (i in 1:length(genes)) {
			curr.gene <- genes[i]
			curr.reg.arr <- genes
			curr.gene.ind <- which(curr.reg.arr == curr.gene)
			if (length(curr.gene.ind) > 0) {
				curr.reg.arr <- curr.reg.arr[-curr.gene.ind]
			}
			
			curr.x <- data[, curr.reg.arr]
			curr.y <- as.numeric(data[, i])
			
			if (length(prob) == 1) {
				prior.prob <- rep(prob, length(genes))
			} else {
				prior.prob <- as.numeric(prob[, curr.gene])
			}
			
			ord <- varord(x = curr.x, y = curr.y, prior.prob = prior.prob, ordering = ordering)
			prior.prob <- prior.prob[ord]
			
			if (algorithm == "ScanBMA") {
				ret.val <- ScanBMA(x = curr.x[, ord[1:nvar]], y = curr.y, control = control, prior.prob = prior.prob[1:nvar])
			} else if (algorithm == "iBMA") {
				ret.val <- iterateBMAlm(x = curr.x[, ord[1:nvar]], y = curr.y, control = control, prior.prob = prior.prob[1:nvar])
			}
			
			if (sum(ret.val$probne0 > 0) > 0) {
				temp.mat <- cbind(ret.val$namesx[ret.val$probne0 > 0], curr.gene, ret.val$probne0[ret.val$probne0 > 0])
				if (ncol(temp.mat) > 1) edge.list <- rbind(edge.list, temp.mat)
			}
		}

		edge.list <- edge.list[as.numeric(edge.list[, 3]) >= postProbThreshold, ]

		parents.arr <- unique(as.character(edge.list[,1]))
		children.arr <- unique(as.character(edge.list[,2]))
		
		out.mat <- NULL
		for (curr.gene in children.arr) {
			curr.ind <- which (as.character(edge.list[,2]) == curr.gene)
			curr.parents.arr <- as.character(edge.list[curr.ind, 1])
			curr.train.dat <- data.frame(data[, curr.parents.arr])
			colnames(curr.train.dat) <- curr.parents.arr
			y.vec <- as.numeric(data[, curr.gene])
			ret.bic.reg <- bicreg(x = curr.train.dat, y = y.vec, nbest = bicreg.nbest, maxCol = bicreg.maxNvar + 1)

			reg.ind <- which (ret.bic.reg$probne0 > 0)
			if (length(reg.ind) > 0) {
				temp.mat <- cbind(ret.bic.reg$namesx[reg.ind], rep(curr.gene, length=length(reg.ind)), ret.bic.reg$probne0[reg.ind], ret.bic.reg$postmean[reg.ind])
				out.mat <- rbind(out.mat, temp.mat)
			}
		}

		edge.list <- out.mat
		
		edge.vec <- paste(edge.list[,1], edge.list[,2], sep=".")
		edge.flipped <- paste(edge.list[,2], edge.list[,1], sep=".")
		flipped.exist <- edge.vec %in% edge.flipped
		
		detail.vec1 <- as.numeric(edge.list[,3])
		detail.vec2 <- as.numeric(edge.list[,4])
		names(detail.vec1) <- edge.vec
		names(detail.vec2) <- edge.vec

		edge.list.sub <- edge.list[flipped.exist==TRUE, ]
		edge.vec.sub <- paste(edge.list.sub[,1], edge.list.sub[,2], sep=".")
		edge.list.noFlipped <- NULL
		done.vec <- rep(0, length(edge.vec.sub))
		
		for (i in 1:length(edge.vec.sub)) {
			if (done.vec[i] == 0) {
				curr.edge <- edge.vec.sub[i]
				curr.flipped.edge <- paste(as.character(edge.list.sub[i, 2]), as.character(edge.list.sub[i, 1]), sep=".")
				flipped.ind <- which (edge.vec.sub == curr.flipped.edge)
				if (abs(detail.vec1[curr.edge]) > abs(detail.vec1[curr.flipped.edge]) || (abs(detail.vec1[curr.edge]) == abs(detail.vec1[curr.flipped.edge]) & abs(detail.vec2[curr.edge]) > abs(detail.vec2[curr.flipped.edge]))) {
					edge.list.noFlipped <- rbind (edge.list.noFlipped, edge.list.sub[i, ])
				} else {
					edge.list.noFlipped <- rbind (edge.list.noFlipped, edge.list.sub[flipped.ind, ]) 
				}
				
				done.vec[i] <- 1
				done.vec[flipped.ind] <- 1
			}
		}

		edge.list.noFlipped <- rbind(edge.list[!flipped.exist,],  edge.list.noFlipped)
		
		curr.edge.list <- edge.list.noFlipped
		all.genes.arr <- unique(c(curr.edge.list[,1], curr.edge.list[,2]))
		detail.vec1 <- as.numeric(curr.edge.list[,3])
		detail.vec2 <- as.numeric(curr.edge.list[,4])
		names(detail.vec1) <- paste(curr.edge.list[,1], curr.edge.list[,2], sep=".")
		names(detail.vec2) <- paste(curr.edge.list[,1], curr.edge.list[,2], sep=".")

		SCC.exist <- 1
		num.SCC.runs <- 0

		while (SCC.exist == 1) {
			g <- graph.empty()
			g <- add.vertices(g, length(all.genes.arr), name=all.genes.arr)

			ids <- 1:length(all.genes.arr)
			names(ids) <- V(g)$name
			from <- as.character(curr.edge.list[,1])
			to <- as.character(curr.edge.list[,2])
			edges <- matrix(c(ids[from], ids[to]), nc=2)
			g <- add.edges(g, t(edges))

			ret.clusters <- clusters (g, mode="strong")

			if (max(ret.clusters$csize) == 1) {
				SCC.exist <- 0
			} else {
				curr.gene.set <- which(ret.clusters$membership == which.max(ret.clusters$csize))
				g2 <- induced.subgraph(g, ids[curr.gene.set])
				exist1.vec <- curr.edge.list[,1] %in% V(g2)$name
				exist2.vec <- curr.edge.list[,2] %in% V(g2)$name
				exist.vec <- exist1.vec & exist2.vec
				min.pp <- min(abs(detail.vec1[exist.vec]))
				min.beta <- min(abs(detail.vec2[abs(detail.vec1)==min.pp & exist.vec]))
				min.ind <- which.max(abs(detail.vec2)==min.beta & exist.vec)
				curr.edge.list <- curr.edge.list[-min.ind,]
				all.genes.arr <- unique(c(curr.edge.list[,1], curr.edge.list[,2]))
				detail.vec1 <- detail.vec1[-min.ind]
				detail.vec2 <- detail.vec2[-min.ind]
			}
		}
		
		out <- data.frame(curr.edge.list, row.names = NULL, stringsAsFactors = FALSE)
		out[, 3] <- as.numeric(out[, 3]) / 100
	}
}, silent=TRUE)

import networkx as nx

if __name__ == "__main__":
	pagerankwriter = open("external_pageRankFile.txt","a")
	G = nx.DiGraph(nx.read_edgelist("edgeList.txt"))

	edgeListReader = open("edgeList.txt","r")

	dictionary = {}
	line = ""

	while(True):
		line = edgeListReader.readline()
		if line == "":
			break
		line = line.rstrip()
		maps = line.split(" ")
		dictionary[maps[0]] = maps[1]

	pr = nx.pagerank(G,alpha=0.85,personalization=None,max_iter=30,tol=1e-06,nstart=None,weight='weight',dangling=None)
	print len(pr)
	for x in pr:
	    if(dictionary.get(x)):
	        pagerankwriter.write(dictionary.get(x)+"="+str(round(pr[x],9))+"\n")
	    else:        
	        pagerankwriter.write(x+"="+str(round(pr[x],9))+"\n")

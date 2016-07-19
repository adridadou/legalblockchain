contract abstract {}

contract owned is abstract {
  address owner;

  function owned() {
  	if(owner != 0) throw;
    owner = currentUser();
  }

  modifier onlyowner() {
    if (currentUser()==owner) _
  }

  function currentUser()(returns address) {
		return msg.sender;
	}
}

contract mortal is abstract, owned {
  function kill() {
    if (msg.sender == owner) suicide(owner);
  }
}

contract LegalContractManagerInterface is mortal, abstract {
	/*
		Get the source of the package. Right now this is an IPFS hash but it should be possible to have any way of retrieving the source (HTTP, FTP etc ...)

		params:
		- Namespace: The namespace of the package you are looking for
		- name: the name of the legal documents
		- version: the version of the legal documents
	*/
	function getSource(string namespace, string name, string version)(returns string);

	/*
		Register a new package. 

		params:
		- Namespace: The namespace of the package you are looking for
		- name: the name of the legal documents
		- version: the version of the legal documents
	*/
	function register(string namespace, string name, string version, string ipfs);

	/*
		Create a new namespace and makes the tx.origin the owner of this namespace
	*/
	function createNamespace(string namespace, address owner);

	/*
		Passes the ownership of a namespace to someone else
	*/
	function changeOwner(string namespace, address newOwner);

	function canWrite(string namespace, address user)(returns bool) constant;
}

contract LegalContractManager is LegalContractManagerInterface {

	private mapping(string => address) owners;
	private mapping(string => mapping(string => mapping(string => string))) context;
	
	function LegalContractManager() {
		owned();
	}

	function getSource(string namespace, string name, string version)(returns string) constant {
		return context[namespace][name][version];
	}

	function canWrite(string namespace, address user)(returns bool) constant {
		return owners[namespace] == user;
	}

	function register(string namespace, string name, string version, string ipfs) {
		if(getSource(namespace,name,version) != "" || !canWrite(namespace,currentUser())) throw;

		context[namespace][name][version] = ipfs;
	}

	function createNamespace(string namespace, address owner) onlyowner {
		if(owners[namespace] != 0) throw;

		owners[namespace] = owner;
	}

	function changeOwner(string namespace, address newOwner){
		if(owners[namespace] != currentUser()) throw;

		owners[namespace] = newOwner;
	}
}


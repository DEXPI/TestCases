Crawler for test case analysis
==============================

Installation (Linux)
--------------------

Open console and type: $ pip3 install -r requirements.txt


Usage (Linux)
-------------

Open console and type: $ python3 process.py


Installation (Windows)
----------------------

Installation for Windows is more tricky because compilation of some libraries (numpy and scipy)
will fail if the system is not set up accordingly. The simplest way is to use precompiled binaries
available online. See https://www.scipy.org/install.html for datails and alternatives.

- Install Python 3 (www.python.org), tested with Python 3.6.1 32 bit.
- Download binaries for numpy an scipy from http://www.lfd.uci.edu/~gohlke/pythonlibs .
  - This site usually contains only the most recent versions. The crawler has been tested
    with numpy 1.12.1 and scipy 0.19.0.
  - Choose adequate download.
    Example: numpy 1.12.1 for Python 3.6.x 32 bit: numpy-1.12.1+mkl-cp36-cp36m-win32.whl
  - Note that '32 bit' refers to the Python installation, not your OS.
- Install first numpy, then scipy from console:
  - Navigate to the directory containing your downloads.
  - Type: PYTHON_DIRECTORY\\Scripts\\pip install NAME_OF_DOWNLOADED_LIB
- Install other libraries from console:
  - Navigate to the directory containing this 'readme.md' and 'requirements.txt'.
  - Type: PYTHON_DIRECTORY\\Scripts\\pip install -r requirements.txt

  
Usage (Windows)
---------------

Open console and type: PYTHON_DIRECTORY\\python process.py

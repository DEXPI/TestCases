import numpy as np
import pandas as pd
import os
import re
import glob

PARTNERS = {
    'AUD': 'Autodesk',
    'AVV': 'Aveva',
    'DXI': 'DEXPI',
    'EVI': 'eVision',
    'EVO': 'Evonik',
    'HUV': 'Heiner Temmen Human Verificator',
    'ING': 'Intergraph',
    'SAG': 'Siemens',
    'VER': 'Verificator',
    'VTT': 'VTT',
    'XVT': 'X-Visual'
}

TESTCASE_NAME_PATTERN = re.compile('\w*\d*(?=V\d*)')
TESTCASE_VERSION_PATTERN = re.compile('(?<=V)\d*')
EXPORT_VERSION_PATTERN = re.compile("(?<=EX)\d+")
IMPORT_VERSION_PATTERN = re.compile('(?<=IM)\d+')
VENDOR_PATTERN = re.compile('|'.join(PARTNERS.keys()))


if __name__ == '__main__':
    # Status
    # 0 - Ok
    # 1 - error
    df = pd.DataFrame({'Test':[], 'Vendor_original': [], 'Vendor_destination': [], 'Status': [], 'Original_version':[],
                       'Destination_version': []})
    exported = glob.glob('./**/*EX[0-9]*.xml', recursive=True)
    # exported = map()
    imported = glob.glob('./**/*EX[0-9]*-*IM[0-9]*.png', recursive=True)

    columns = ['TestCase', 'VendorFrom', 'TestCaseVersion', 'ExportVersion']
    exported_df = pd.DataFrame(columns=columns)

    for item in exported:
        name = os.path.basename(item)
        test_case = TESTCASE_NAME_PATTERN.findall(name)[0]
        test_case_version = TESTCASE_VERSION_PATTERN.findall(name)[0]
        vendors = VENDOR_PATTERN.findall(name)
        exported_version = EXPORT_VERSION_PATTERN.findall(name)[0]
        exported_df.loc[len(exported_df)] = [test_case, vendors[0], test_case_version, exported_version]

    print(exported_df)

    columns = ['TestCase', 'VendorFrom', 'TestCaseVersion', 'ExportVersion', 'VendorTo', 'ImportVersion']

    imported_df = pd.DataFrame(columns=columns)

    for item in imported:
        name = os.path.basename(item)
        test_case = TESTCASE_NAME_PATTERN.findall(name)[0]
        test_case_version = TESTCASE_VERSION_PATTERN.findall(name)[0]
        vendors = VENDOR_PATTERN.findall(name)
        exported_version = EXPORT_VERSION_PATTERN.findall(name)[0]
        imported_version = IMPORT_VERSION_PATTERN.findall(name)[0]
        imported_df.loc[len(imported_df)] = [test_case, vendors[0], test_case_version, exported_version, vendors[1], imported_version]

    print(imported_df)








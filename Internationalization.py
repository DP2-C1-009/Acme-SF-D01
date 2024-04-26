import os
import re

def get_jsp_code(directory_path,excluded_path):
    codes = set()
    pattern = r'code\s*=\s*["\']([^"\']+)["\']'
    for root, _, files in os.walk(directory_path):
        root = root.replace("\\", "/")
        if root.startswith(excluded_path):
            continue
        else:
            for file in files:
                if file.endswith(".jsp"):
                    file_path = os.path.join(root, file)
                    with open(file_path, 'r') as f:
                        content = f.read()
                        matches = re.findall(pattern, content)

                        for match in matches:
                            if not "${" in match:
                                
                                codes.add(match)
    return codes

def get_eni18n_code(directory_path,excluded_path):
    codes = set()
    for root, _, files in os.walk(directory_path):
        root = root.replace("\\", "/")
        if root.startswith(excluded_path) :
            continue
        else:
            for file in files:
                if file.endswith("en.i18n"):
                    file_path = os.path.join(root, file)
                    with open(file_path, 'r') as f:
                        content = f.read()
                        for line in content.splitlines():
                            # Ignora líneas que comienzan con "#"
                            if not line.startswith("#")  and  "=" in line:
                                code = line.split("=")[0].strip()
                                
                                codes.add(code)
       
    return codes

def get_esi18n_code(directory_path,excluded_path):
    codes = set()
    for root, _, files in os.walk(directory_path):
        root = root.replace("\\", "/")
        if root.startswith(excluded_path) :
            continue
        else:
            for file in files:
                if file.endswith("es.i18n"):
                    file_path = os.path.join(root, file)
                    with open(file_path, 'r') as f:
                        content = f.read()
                        for line in content.splitlines():
                            # Ignora líneas que comienzan con "#"
                            if not line.startswith("#")  and  "=" in line:
                                code = line.split("=")[0].strip()
                                
                                codes.add(code)
       
    return codes

def compare_sets(set1, set2):
    
    # Obtenemos los elementos que están en set1 pero no en set2
    difference = set1 - set2
    
    if len(difference) == 0:
        print("No hay ningun code sin traducir en jsp")
    else:
        print("Quedan code/s por traducir en jsp: ", difference)
    
    #Descomentar para comprobar si quedan codes en archivos .i18n
    #difference = set2 - set1
    #
    # if len(difference) == 0:
    #     print("No hay ningun code sin traducir en i18n")
    # else:
    #     print("Quedan code/s por traducir en i18n: ", difference)
    
    return difference

if __name__ == "__main__":
    #Ruta de tu proyecto Acme
    directory_path = os.path.dirname(os.path.abspath(__file__))
    
    #Ruta de la carpeta target del proyecto
    excluded_path = os.path.dirname(os.path.abspath(__file__)) + "/target"

    set1 = get_jsp_code(directory_path,excluded_path)
    set2 = get_eni18n_code(directory_path,excluded_path)
    set3 = get_esi18n_code(directory_path,excluded_path)

    compare_sets(set1,set2)
    compare_sets(set1,set3)

from pathlib import Path
import os
import ctypes, sys

def is_admin():
    try:
        return ctypes.windll.shell32.IsUserAnAdmin()
    except:
        return False

def create_visab_bat(dir, is_gui):
    path = str(base_dir) + '\\visab.bat'
    if not is_gui:
        path = str(base_dir) + '\\visab_headless.bat'
    
    with open(path, 'w+') as f:
        if is_gui:
            f.write(f'java -jar {full_jar[0].absolute()} -mode gui')
        else:
            f.write(f'java -jar {full_jar[0].absolute()} -mode headless')
        print(f'Wrote new batchfile at {path}')


jars = [jar for jar in Path('.').rglob('*.jar')]
if jars:
    full_jar = [jar for jar in jars if 'original' not in str(jar)]
    if full_jar:
        base_dir = full_jar[0].parents[0].absolute()
        print(f'Found jar file {full_jar[0].name} in {base_dir}')
        
        create_visab_bat(base_dir, True)
        create_visab_bat(base_dir, False)

        # Only set if it isn't on path already
        if str(base_dir) in os.environ['PATH']:
            exit()
        
        if is_admin():            
            os.system(f'setx PATH "%PATH%;{base_dir}" /M')
            print(f'Set new PATH variable for {base_dir}')
        else:
            print('Need admin privileges for setting systemvariable. Restarting as admin...')
            ctypes.windll.shell32.ShellExecuteW(None, 'runas', sys.executable, ' '.join(sys.argv), None, 1)
else:
    print(f'Couldent recoursively find any jar in {Path(".").absolute()}')
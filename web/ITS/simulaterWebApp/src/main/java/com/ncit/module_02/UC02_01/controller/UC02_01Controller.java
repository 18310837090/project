package com.ncit.module_02.UC02_01.controller;

import com.ncit.common.Constants;
import com.ncit.common.model.RemoteProperties;
import com.ncit.common.util.fileUtil;
import com.ncit.module_02.UC02_01.model.UC02_01_SetParameterInput;
import com.ncit.module_init.model.UC_Init_loginInput;
import com.ncit.module_init.model.UC_Init_userInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.yaml.snakeyaml.Yaml;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.yaml.snakeyaml.parser.ParserException;
import org.yaml.snakeyaml.scanner.ScannerException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.ncit.common.util.StringUtil.getOldValueStr;

@Controller
public class UC02_01Controller {
    @Resource
    RemoteProperties remoteProperties;

    /**
     * 画面初始化
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/setParameterInit", method = RequestMethod.GET)
    public ModelAndView setParameterInit(HttpServletRequest request) {
        HttpSession session = request.getSession();
        ModelAndView result = new ModelAndView();
        UC_Init_userInfo userInfo = (UC_Init_userInfo) session.getAttribute("userInfo");
        if (userInfo == null) {
            result.addObject("userInfo", new UC_Init_loginInput());
            result.setViewName(Constants.PAGE_MODULE_INIT_010);
            return result;
        }
        // 世代数
        String generation_max = "";
        // 人口数
        String population_size = "";
        // 人口择优范围
        String tournament_size = "";
        // 模拟时间
        String sim_duration = "";
        // 评价指标
        String goal = "";

        // 读文件　guiyang-1sim_pg.yml
        try {
            Yaml yaml = new Yaml();
            File dumpFile = new File(remoteProperties.getPATH_1());

            // 读文件 guiyang-1sim_pg.yml
            FileInputStream fis = new FileInputStream(dumpFile);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                // System.out.println("line : " + line);

                // 世代数，人口数，人口择优范围 有一个为空字符串，就解析读入的文件行
                try {
                    Object obj = yaml.load(line);
                    if (obj instanceof Map) {
                        Map<String, Object> objMap = (LinkedHashMap<String, Object>) obj;
                        for (String key : objMap.keySet()) {
                            if (StringUtils.equals(key, Constants.GENERATION_MAX)) {
                                generation_max = getOldValueStr(line);
                            } else if (StringUtils.equals(key, Constants.POPULATION_SIZE)) {
                                population_size = getOldValueStr(line);
                            } else if (StringUtils.equals(key, Constants.TOURNAMENT_SIZE)) {
                                tournament_size = getOldValueStr(line);
                            } else {
                                continue;
                            }
                        }
                    }

                    if (StringUtils.isNotBlank(generation_max)
                            && StringUtils.isNotBlank(population_size)
                            && StringUtils.isNotBlank(tournament_size)) {
                        break;
                    }
                } catch (ScannerException e) {
                    e.printStackTrace();
                } catch (ParserException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            br.close();
            isr.close();
            fis.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 读文件　sim-configuration.yml
        try {
            Yaml yaml = new Yaml();
            File dumpFile = new File(remoteProperties.getPATH_2());

            FileInputStream fis = new FileInputStream(dumpFile);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line = "";
            while ((line = br.readLine()) != null) {

                // 世代数，人口数，人口择优范围 有一个为空字符串，就解析读入的文件行
                try {
                    Object obj = yaml.load(line);
                    if (obj instanceof Map) {
                        Map<String, Object> objMap = (LinkedHashMap<String, Object>) obj;
                        for (String key : objMap.keySet()) {
                            if (StringUtils.equals(key, Constants.SIM_DURATION)) {
                                sim_duration = getOldValueStr(line);
                            } else if (StringUtils.equals(key, Constants.GOAL)) {
                                goal = getOldValueStr(line);
                            } else {
                                continue;
                            }
                        }
                    }

                    if (StringUtils.isNotBlank(sim_duration)
                            && StringUtils.isNotBlank(goal)) {
                        break;
                    }
                } catch (ScannerException e) {
                    e.printStackTrace();
                } catch (ParserException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            br.close();
            isr.close();
            fis.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 世代数
        result.addObject("generation_max", generation_max);
        // 人口数
        result.addObject("population_size", population_size);
        // 人口择优范围
        result.addObject("tournament_size", tournament_size);
        // 模拟时间
        result.addObject("sim_duration", sim_duration);
        // 评价指标
        result.addObject("goal", goal);

        result.setViewName(Constants.PAGE_MODULE_02_010);
        return result;
    }

    /**
     * 递归取Yaml的值
     *
     * @param _map
     * @param _key
     * @return
     */
    public static String getYamlValue(Map<String, Object> _map, String _key) {
        System.out.println("★★★getValue start");
        String value = "";
        if (_map == null || _map.isEmpty()) {
            return null;
        }

        for (String key : _map.keySet()) {
            if (StringUtils.equals(key, _key)) {
                Object obj = _map.get(key);
                return String.valueOf(_map.get(key));
            } else {
                Object obj = _map.get(key);
                if (obj != null && obj instanceof Map) {
                    return getYamlValue((Map<String, Object>)obj, _key);
                } else {
                    continue;
                }
            }
        }
        return value;
    }


    /**
     * 参数设定
     *
     * @param input
     * @param request
     * @return
     */
    @RequestMapping(value = "/setParameter", method = RequestMethod.POST)
    public ModelAndView setParameter(@ModelAttribute(value = "uc02_01_SetParameterInput") UC02_01_SetParameterInput input, HttpServletRequest request) {
        HttpSession session = request.getSession();
        ModelAndView result = new ModelAndView();
        UC_Init_userInfo userInfo = (UC_Init_userInfo) session.getAttribute("userInfo");
        if (userInfo == null) {
            result.addObject("userInfo", new UC_Init_loginInput());
            result.setViewName(Constants.PAGE_MODULE_INIT_010);
            return result;
        }

        // 画面入力的参数值
        String generation_max_input = StringUtils.trim(input.getGeneration_max());
        String population_size_input = StringUtils.trim(input.getPopulation_size());
        String tournament_size_input = StringUtils.trim(input.getTournament_size());
        String sim_duration_input = StringUtils.trim(input.getSim_duration());
        String goal_input = StringUtils.trim(input.getGoal());

        // 配置文件中的参数值
        String generation_max_old;
        String population_size_old;
        String population_opt_size_old;
        String sim_duration_old;
        String goal_old;

        boolean generation_max_flag= true;
        boolean population_size_flag = true;
        boolean population_opt_size_flag = true;
        boolean sim_duration_flag = true;
        boolean goal_flag = true;

        boolean success = true;

        // 入力检查

        try {

            File dumpFile1 = new File(remoteProperties.getPATH_1());
            if(dumpFile1.exists()) {
                // 利用yaml包修改文件，但是会使文件格式变化，文件的注视消失，所以不用这种方法了
//                //获取test.yaml文件中的配置数据，然后转换为obj，
//                Object loadInfos =yaml.load(new FileInputStream(dumpFile1));

//                FileWriter fileWriter = new FileWriter(dumpFile1);
//                fileWriter.write(yaml.dump(loadInfos));
//                fileWriter.flush();
//                fileWriter.close();

                // 读文件 guiyang-1sim_pg.yml
                FileInputStream fis = new FileInputStream(dumpFile1);
                InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
                BufferedReader br = new BufferedReader(isr);

                String line = "";
                List<String> lb = new ArrayList<String>();
                boolean updateFlg = false;
                while ((line = br.readLine()) != null) {
                    // System.out.println("line : " + line);

                    // 世代数，人口数，人口择优范围 有一个为空字符串，就解析读入的文件行
                    if (generation_max_flag
                            || population_size_flag
                            || population_opt_size_flag) {
                        try {
                            Yaml yaml = new Yaml();
                            Object obj = yaml.load(line);
                            if (obj instanceof Map) {
                                Map<String, Object> objMap = (LinkedHashMap<String, Object>) obj;
                                for (String key : objMap.keySet()) {
                                    if (StringUtils.equals(key, Constants.GENERATION_MAX)) {
                                        generation_max_flag = false;
                                        generation_max_old = String.valueOf(objMap.get(key));
                                        if (!StringUtils.equals(generation_max_input, generation_max_old)) {
                                            updateFlg = true;
                                            line = getNewStr(line, generation_max_input);
                                        }
                                    } else if (StringUtils.equals(key, Constants.POPULATION_SIZE)) {
                                        population_size_flag = false;
                                        population_size_old = String.valueOf(objMap.get(key));
                                        if (!StringUtils.equals(population_size_input, population_size_old)) {
                                            updateFlg = true;
                                            line = getNewStr(line, population_size_input);
                                        }
                                    } else if (StringUtils.equals(key, Constants.TOURNAMENT_SIZE)) {
                                        population_opt_size_flag = false;
                                        population_opt_size_old = String.valueOf(objMap.get(key));
                                        if (!StringUtils.equals(tournament_size_input, population_opt_size_old)) {
                                            updateFlg = true;
                                            line = getNewStr(line, tournament_size_input);
                                        }
                                    } else {
                                        continue;
                                    }
                                }
                            }
                        } catch (ScannerException e) {
                            e.printStackTrace();
                        } catch (ParserException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    lb.add(line);
                }
                br.close();
                isr.close();
                fis.close();

                // 写文件
                if (!updateFlg) {
                    // TODO 值不变，未作修改 (警告信息)

                } else {
                    FileOutputStream fos = new FileOutputStream(new File(remoteProperties.getPATH_1_NEW()));
                    OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
                    BufferedWriter bw = new BufferedWriter(osw);
                    for(String arr : lb){
                        bw.write(arr + "\n");
                    }
                    bw.close();
                    osw.close();
                    fos.close();

                    if (!fileUtil.moveFile(remoteProperties.getPATH_1_NEW(), remoteProperties.getPATH_1(), true)) {
                        success = false;
                    }
                }

            } else {
                // TODO 文件不存在的处理 (警告信息)
                success = false;
            }

            File dumpFile2 = new File(remoteProperties.getPATH_2());
            if(dumpFile2.exists()) {
                // 读文件 sim-configuration.yml
                FileInputStream fis = new FileInputStream(dumpFile2);
                InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
                BufferedReader br = new BufferedReader(isr);

                String line;
                List<String> lb = new ArrayList<String>();
                boolean updateFlg = false;
                while ((line = br.readLine()) != null) {
                    // System.out.println("line : " + line);

                    // 世代数，人口数，人口择优范围 有一个为空字符串，就解析读入的文件行
                    if (sim_duration_flag || goal_flag) {
                        try {
                            Yaml yaml = new Yaml();
                            Object obj = yaml.load(line);
                            if (obj instanceof Map) {
                                Map<String, Object> objMap = (LinkedHashMap<String, Object>) obj;
                                for (String key : objMap.keySet()) {
                                    if (StringUtils.equals(key, Constants.SIM_DURATION)) {
                                        sim_duration_flag = false;
                                        sim_duration_old = String.valueOf(objMap.get(key));
                                        if (!StringUtils.equals(sim_duration_input, sim_duration_old)) {
                                            updateFlg = true;
                                            line = getNewStr(line, sim_duration_input);
                                        }
                                    } else if (StringUtils.equals(key, Constants.GOAL)) {
                                        goal_flag = false;
                                        goal_old = String.valueOf(objMap.get(key));
                                        if (!StringUtils.equals(goal_input, goal_old)) {
                                            updateFlg = true;
                                            line = getNewStr(line, goal_input);
                                        }
                                    } else {
                                        continue;
                                    }
                                }
                            }
                        } catch (ScannerException e) {
                            e.printStackTrace();
                        } catch (ParserException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    lb.add(line);
                }
                br.close();
                isr.close();
                fis.close();

                // 写文件

                if (!updateFlg) {
                    // TODO 值不变，未作修改 (警告信息)

                } else {
                    FileOutputStream fos = new FileOutputStream(new File(remoteProperties.getPATH_2_NEW()));
                    OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
                    BufferedWriter bw = new BufferedWriter(osw);
                    for(String arr : lb){
                        bw.write(arr + "\n");
                    }
                    bw.close();
                    osw.close();
                    fos.close();

                    if (!fileUtil.moveFile(remoteProperties.getPATH_2_NEW(), remoteProperties.getPATH_2(), true)) {
                        success = false;
                    }
                }

            } else {
                // TODO 文件不存在的处理 (警告信息)
                success = false;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            success = false;
        } catch (IOException e) {
            e.printStackTrace();
            success = false;
        }

        if (success) {
            result.addObject("message", Constants.Message_Module_02_010_OK_001);
        } else {
            result.addObject("message", Constants.Message_Module_02_010_NG_001);
        }

        // 世代数
        result.addObject("generation_max", generation_max_input);
        // 人口数
        result.addObject("population_size", population_size_input);
        // 人口择优范围
        result.addObject("tournament_size", tournament_size_input);
        // 模拟时间
        result.addObject("sim_duration", sim_duration_input);
        // 评价指标
        result.addObject("goal", goal_input);

        result.setViewName(Constants.PAGE_MODULE_02_010);
        return result;
    }

    /**
     * 编辑字符串，加入新值
     *
     * @param _oldStr
     * @param _value
     * @return
     */
    public static String getNewStr(String _oldStr, String _value) {
        String newStr = _oldStr;

        int startIndex = StringUtils.indexOf(_oldStr, ":");
        int bIndex = StringUtils.indexOf(_oldStr, "#");

        if (startIndex != -1) {
            StringBuilder sb = new StringBuilder();
            sb.append(StringUtils.substring(_oldStr, 0, startIndex + 1));
            sb.append(" ");
            sb.append(_value);

            if (bIndex != -1) {
                sb.append("    ");
                sb.append(StringUtils.substring(_oldStr, bIndex));
            }

            newStr = sb.toString();
        }
        return newStr;
    }


}

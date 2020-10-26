import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UsccValidate{

	static Map<Character,List<Character>> oneTwoMap = Stream.of(new Object[][]{
			{'1',new Character[]{'1','2','3','9'}},
			{'2',new Character[]{'1','9'}},
			{'3',new Character[]{'1','2','3','4','5','9'}},
			{'4',new Character[]{'1','9'}},
			{'5',new Character[]{'1','2','3','9'}},
			{'6',new Character[]{'1','2','9'}},
			{'7',new Character[]{'1','2','9'}},
			{'8',new Character[]{'1','9'}},
			{'9',new Character[]{'1','2','3'}},
			{'A',new Character[]{'1','9'}},
			{'N',new Character[]{'1','2','3','9'}},
			{'Y',new Character[]{'1'}}
	}).collect(Collectors.toMap(data -> (Character) data[0], data -> (List<Character>) Arrays.asList((Character[])data[1]))); //前两位对应关系

	static int weight[]= {1,3,9,27,19,26,16,17,20,29,25,13,8,24,10,30,28};		//各位置序号上的加权因子

	static Map<Character,Integer> charMapInt = Stream.of(new Object[][]{
		{'0',0},{'1',1},{'2',2},{'3',3},{'4',4},{'5',5},{'6',6},{'7',7},{'8',8},{'9',9},
		{'A',10},{'B',11},{'C',12},{'D',13},{'E',14},{'F',15},{'G',16},{'H',17},{'J',18},{'K',19},{'L',20},{'M',21},
		{'N',22},{'P',23},{'Q',24},{'R',25},{'T',26},{'U',27},{'W',28},{'X',29},{'Y',30}
	}).collect(Collectors.toMap(data -> (Character) data[0], data -> (Integer) data[1])); //GB32100-2015标准代码字符集

	/*
	[函数名]checkUSCC
	[功能]校验18位统一社会信用代码正确性
	[参数]testUSCC:待校验的统一社会信用代码（要求字母已经保持大写）
	[返回值]boolean类型，0(false)表示验证未通过，1(true)表示验证通过
	*/
	public static boolean checkUSCC(String testUSCC){
		if(testUSCC.length()!=18)
		{
			System.out.println("统一社会信用代码长度错误");
			return false;
		}
		
		if(!testTopTwo(testUSCC.substring(0, 2))){
			return false;
		}

		int Top17Sum = 0;														//用于存放代码字符和加权因子乘积之和

		for(int index = 0;index < 17;index++){
			
			Integer intIndex;
			char charIndex = testUSCC.charAt(index);
			intIndex = charMapInt.get(charIndex);
			if(intIndex == null){											//验证代码中是否有错误字符
				System.out.println("统一社会信用代码中出现错误字符");
				return false;
			}
			Top17Sum += intIndex*weight[index];
		}
		int expectNum = 31 - Top17Sum % 31;
		if(expectNum == 31){
			expectNum = 0;
		}
		return charMapInt.get(testUSCC.charAt(17)) == expectNum ? true : false;	//按照GB 32100—2015标准对统一社会信用代码前17位计算校验码，并与第18位校验位进行比对
	}

	static boolean testTopTwo(String sec){
		assert sec != null && sec.length() == 2;
		char first = sec.charAt(0);
		char second = sec.charAt(1);
		int pos = oneTwoMap.get(first).indexOf(second);
		return pos == -1 ? false : true;
	}

	public static void main(String[] args) {
		String test[]= {"51420000MJH2003604","51420000MJH200408C","52420000MJH233410E",
            "51420000MJH200395N","51420000MJH2003791","53420000MJH2448303",
            "52420000MJH233402K","52420000MJH2333813","52420000MJH23339XT",
            "51420000MJH200387U"};
        for(int i=0;i<10;i++){
            if(UsccValidate.checkUSCC(test[i]))
                System.out.println("验证通过");
            else
                System.out.println("验证失败");
        }
	}
}

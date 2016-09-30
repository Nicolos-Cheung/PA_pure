<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>pcm</title>
</head>
<body>
	<div align="center">
		<form name="uploadForm" method="POST" enctype="MULTIPART/FORM-DATA"
			action="${pageContext.request.contextPath}/pcm_register">
			<table>
				<tr>
					<td>声纹注册</td>
				</tr>
				<tr>
					<td><div align="left">user_id</div>
					</td>
					<td><input type="text" id="user_id" name="user_id" size="20"
						value="123456" /></td>
				</tr>
				<tr>
					<td><div align="left">person_id</div>
					</td>
					<td><input type="text" id="person_id" name="person_id"
						size="20" value="123456" />
					</td>
				</tr>
				<tr>
					<td><div align="left">telnum</div>
					</td>
					<td><input type="text" id="telnum" name="telnum" size="20"
						value="18896932171" /></td>
				</tr>
				<tr>
					<td><div align="left">source</div>
					</td>
					<td><input type="text" id="source" name="source" size="20"
						value="1" /></td>
				</tr>
				<tr>
					<td><div align="left">policy_number</div>
					</td>
					<td><input type="text" id="policy_number" name="policy_number"
						value="368028201028201" size="20" />
					</td>
				</tr>
				<tr>
					<td><div align="left">nas_dir</div>
					</td>
					<td><input type="text" id="nas_dir" name="nas_dir" size="20"
						value="/Users/ning/xxx/xx" /></td>
				</tr>
				<tr>
					<td><div align="left">response_num</div>
					</td>
					<td><input type="text" id="response_num" name="response_num"
						value="090256789019876" size="20" />
					</td>
				</tr>
				<tr>
					<td><div align="left" size="20">音频文件:</div>
					</td>
					<td><input type="file" name="file" size="20" /></td>
				</tr>
				<tr>
					<td><input type="submit" name="submit" value="register">
					</td>
					<td><input type="reset" name="reset" value="reset">
					</td>
				</tr>
			</table>
		</form>

		<form name="uploadForm" method="POST" enctype="MULTIPART/FORM-DATA"
			action="${pageContext.request.contextPath}/pcm_update">
			<table>
				<tr>
					<td>声纹更新</td>
				</tr>
				<tr>
					<td><div align="left">user_id</div>
					</td>
					<td><input type="text" id="user_id" name="user_id" size="20"
						value="123456" /></td>
				</tr>
				<tr>
					<td><div align="left">person_id</div>
					</td>
					<td><input type="text" id="person_id" name="person_id"
						size="20" value="123456" />
					</td>
				</tr>
				<tr>
					<td><div align="left">telnum</div>
					</td>
					<td><input type="text" id="telnum" name="telnum" size="20"
						value="18896932171" /></td>
				</tr>
				<tr>
					<td><div align="left">source</div>
					</td>
					<td><input type="text" id="source" name="source" size="20"
						value="1" /></td>
				</tr>
				<tr>
					<td><div align="left">policy_number</div>
					</td>
					<td><input type="text" id="policy_number" name="policy_number"
						value="368028201028201" size="20" />
					</td>
				</tr>
				<tr>
					<td><div align="left">nas_dir</div>
					</td>
					<td><input type="text" id="nas_dir" name="nas_dir" size="20"
						value="/Users/ning/xxx/xx" /></td>
				</tr>
				<tr>
					<td><div align="left">response_num</div>
					</td>
					<td><input type="text" id="response_num" name="response_num"
						value="090256789019876" size="20" />
					</td>
				</tr>
				<tr>
					<td><div align="left" size="20">音频文件:</div>
					</td>
					<td><input type="file" name="file" size="20" /></td>
				</tr>
				<tr>
					<td><input type="submit" name="submit" value="register">
					</td>
					<td><input type="reset" name="reset" value="reset">
					</td>
				</tr>
			</table>
		</form>

		<form name="uploadForm" method="POST" enctype="MULTIPART/FORM-DATA"
			action="${pageContext.request.contextPath}/pcm_validation">
			<table>
				<tr>
					<td>声纹验证</td>
				</tr>
				<tr>
					<td><div align="left">person_id</div>
					</td>
					<td><input type="text" size="20" name="person_id"
						id="person_id" value="123456" /> <span id="msg"></span></td>
				</tr>
				<tr>
					<td><div align="left">response_num</div>
					</td>
					<td><input type="text" id="response_num" name="response_num"
						value="090256789019876" size="20" />
					</td>
				</tr>

				<tr>
					<td><div align="left">音频文件:</div>
					</td>
					<td><input type="file" name="file" size="20" /></td>
				</tr>
				<tr>
					<td><input type="submit" name="submit" value="validation">
					</td>
					<td><input type="reset" name="reset" value="reset">
					</td>
				</tr>
			</table>
		</form>
</body>
</html>
